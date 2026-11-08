package com.sparkstart.app.content

import com.sparkstart.app.data.*

enum class HitKind(val label: String) { LESSON("📖 Lesson"), FIX("🛠️ Fault-finding"), QUIZ("❓ Quiz"), CARD("🃏 Flashcard") }

class SearchEntry(
    val kind: HitKind,
    val title: String,
    val where: String,
    val body: String,
    val lessonId: String?,
    val page: Int,
    val quiz: Quiz? = null,
    val card: Flashcard? = null
) {
    internal val titleTokens: Set<String> = SearchIndex.tokens(title)
    internal val bodyTokens: Set<String> = SearchIndex.tokens(body)
    internal val norm: String = SearchIndex.normalize("$title $body")
}

class SearchHit(val entry: SearchEntry, val score: Float, val snippet: String, val matched: Set<String>)

/** Offline search over every lesson page, fault-finding step, quiz and flashcard. */
object SearchIndex {

    private val stop = setOf(
        "how", "to", "a", "an", "the", "with", "do", "does", "i", "what", "why", "is", "are", "my", "on", "in", "of",
        "for", "and", "or", "it", "can", "you", "when", "which", "should", "that", "this", "from", "at", "be", "get",
        "about", "into", "its", "your", "if", "me", "we", "they", "there", "was", "will", "would", "work", "works", "working"
    )

    /** Groups of words that mean the same thing on site. Every word is stemmed before lookup. */
    private val synonymGroups: List<Set<String>> = listOf(
        setOf("el", "leakage", "elr", "rcd", "rcbo", "toroid", "elcb"),
        setOf("ol", "overload", "thermal"),
        setOf("powerboard", "board", "switchboard", "db", "distribution"),
        setOf("gpo", "outlet", "socket", "powerpoint", "receptacle", "plug"),
        setOf("megger", "insulation", "ir"),
        setOf("pilot", "continuity", "ec", "diode"),
        setOf("fix", "repair", "fault", "troubleshoot", "faultfind"),
        setOf("trip", "trips", "tripping", "nuisance"),
        setOf("pump", "flygt", "mono", "submersible", "dewatering", "snore"),
        setOf("jumbo", "rig", "drill"),
        setOf("breaker", "mcb", "cb", "mccb"),
        setOf("cable", "lead", "cord", "trailing"),
        setOf("earth", "earthing", "ground", "men"),
        setOf("motor", "winding"),
        setOf("test", "tester", "check", "measure", "prove"),
        setOf("meter", "multimeter", "voltmeter"),
        setOf("clamp", "ammeter"),
        setOf("shock", "electrocution"),
        setOf("isolate", "isolation", "lockout", "lock", "tag", "loto"),
        setOf("fan", "ventilation", "vent"),
        setOf("light", "lighting", "lamp"),
        setOf("volt", "voltage"),
        setOf("amp", "current"),
        setOf("start", "starter", "wont")
    ).map { g -> g.map { stem(it) }.toSet() }

    private val synonyms: Map<String, Set<String>> = buildMap {
        for (g in synonymGroups) for (w in g) put(w, (get(w) ?: emptySet()) + g)
    }

    private val fixWords = setOf("fix", "repair", "fault", "troubleshoot", "trip", "wont", "faultfind", "find").map { stem(it) }.toSet()

    fun normalize(s: String): String =
        s.lowercase()
            .replace("e/l", "el").replace("o/l", "ol").replace("won't", "wont").replace("can't", "cant")
            .replace("power board", "powerboard").replace("power point", "powerpoint")
            .replace("fault find", "faultfind").replace("fault-find", "faultfind")
            .replace("earth leakage", "el leakage").replace(Regex("[^a-z0-9]+"), " ")
            .trim()

    fun stem(w0: String): String {
        var w = w0
        if (w.length > 5 && w.endsWith("ing")) {
            w = w.dropLast(3)
            if (w.length > 2 && w[w.lastIndex] == w[w.lastIndex - 1]) w = w.dropLast(1)
        } else if (w.length > 4 && w.endsWith("ed")) {
            w = w.dropLast(2)
            if (w.length > 2 && w[w.lastIndex] == w[w.lastIndex - 1]) w = w.dropLast(1)
        } else if (w.length > 4 && (w.endsWith("xes") || w.endsWith("ches") || w.endsWith("shes"))) {
            w = w.dropLast(2)
        } else if (w.length > 3 && w.endsWith("s") && !w.endsWith("ss")) {
            w = w.dropLast(1)
        }
        return w
    }

    fun tokens(s: String): Set<String> = normalize(s).split(' ').filter { it.isNotBlank() }.map { stem(it) }.toSet()

    private fun blockText(b: Block): String = when (b) {
        is P -> b.text
        is Bullets -> b.items.joinToString("\n")
        is Tip -> b.text
        is Hazard -> b.text
        is WhyNot -> b.text
        is FaultFind -> b.steps.joinToString("\n")
        is Std -> "${b.ref}: ${b.text}"
        is Ask -> b.text
        is Vis -> b.caption + " " + b.visual.name.lowercase().replace('_', ' ')
        is Deep -> b.text
        is Quiz -> b.q + "\n" + b.options.joinToString(" · ") + "\n" + b.why
        is Order -> b.prompt + "\n" + b.steps.joinToString("\n")
    }

    val entries: List<SearchEntry> by lazy {
        val out = ArrayList<SearchEntry>()
        val moduleOfLesson = HashMap<String, String>()
        for (m in Course.modules) {
            for (l in m.lessons) {
                moduleOfLesson[l.id] = m.id
                l.pages.forEachIndexed { pi, pg ->
                    val hasFix = pg.blocks.any { it is FaultFind }
                    val body = pg.blocks.joinToString("\n") { blockText(it) }
                    out += SearchEntry(
                        if (hasFix) HitKind.FIX else HitKind.LESSON,
                        "${l.emoji} ${l.title} › ${pg.title}",
                        "${m.emoji} ${m.title} · ${l.level.emoji} ${l.level.label}",
                        body, l.id, pi
                    )
                    pg.blocks.filterIsInstance<Quiz>().forEach { q ->
                        out += SearchEntry(HitKind.QUIZ, q.q, "${l.emoji} ${l.title}", q.options.joinToString(" · ") + "\n" + q.why, l.id, pi, quiz = q)
                    }
                }
            }
        }
        for ((mid, q) in Course.extraQuizzes) {
            val m = Course.module(mid)
            out += SearchEntry(HitKind.QUIZ, q.q, m?.let { "${it.emoji} ${it.title}" } ?: "Quiz", q.options.joinToString(" · ") + "\n" + q.why, null, 0, quiz = q)
        }
        for (c in Course.flashcards) {
            val m = Course.module(c.moduleId)
            out += SearchEntry(HitKind.CARD, c.front, m?.let { "${it.emoji} ${it.title}" } ?: "Flashcard", c.back, null, 0, card = c)
        }
        out
    }

    fun search(query: String, kinds: Set<HitKind>? = null, limit: Int = 60): List<SearchHit> {
        val qNorm = normalize(query)
        val qTokens = qNorm.split(' ').filter { it.isNotBlank() && it !in stop }.map { stem(it) }.distinct()
        if (qTokens.isEmpty()) return emptyList()
        val wantsFix = qTokens.any { it in fixWords }
        val groups = qTokens.map { t -> t to (synonyms[t] ?: setOf(t)) }
        val needed = if (groups.size <= 2) groups.size else (groups.size * 0.6f).toInt().coerceAtLeast(2)
        val hits = ArrayList<SearchHit>()
        for (e in entries) {
            if (kinds != null && e.kind !in kinds) continue
            var matchedGroups = 0
            var score = 0f
            val matched = HashSet<String>()
            for ((orig, alts) in groups) {
                var best = 0f
                for (a in alts) {
                    val w = if (a == orig) 1f else 0.6f
                    val prefixHit = orig.length >= 4 && a == orig && (e.titleTokens.any { it.startsWith(a) } || e.bodyTokens.any { it.startsWith(a) })
                    val s = when {
                        a in e.titleTokens -> 6f * w
                        a in e.bodyTokens -> 1.5f * w
                        prefixHit -> 1f * w
                        else -> 0f
                    }
                    if (s > 0f) matched += a
                    if (s > best) best = s
                }
                if (best > 0f) { matchedGroups++; score += best }
            }
            if (matchedGroups < needed) continue
            score += matchedGroups * 10f
            if (qTokens.size > 1 && e.norm.contains(qTokens.joinToString(" "))) score += 15f
            if (wantsFix && e.kind == HitKind.FIX) score += 6f
            if (e.kind == HitKind.QUIZ || e.kind == HitKind.CARD) score -= 1f
            hits += SearchHit(e, score, snippet(e.body, matched), matched)
        }
        return hits.sortedByDescending { it.score }.take(limit)
    }

    private fun snippet(body: String, matched: Set<String>): String {
        val lines = body.split('\n').filter { it.isNotBlank() }
        val best = lines.maxByOrNull { line -> tokens(line).count { it in matched } } ?: return ""
        return if (best.length > 220) best.take(217) + "…" else best
    }

    val suggestions = listOf(
        "e/l fault", "pump overload trip", "how to test a power board", "pilot trip", "insulation resistance test",
        "RCD test", "jumbo won't start", "voltage drop", "star delta", "earth continuity", "isolation lock out",
        "fix pump with earth leakage", "overload setting calculation", "MEN system", "test for dead"
    )
}
