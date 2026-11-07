package com.sparkstart.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.content.Course
import com.sparkstart.app.data.Quiz
import com.sparkstart.app.data.Store
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong
import kotlin.random.Random

@Composable
private fun HubCard(icon: String, title: String, sub: String, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 30.sp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(sub, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun PracticeHubScreen(
    store: Store,
    onQuiz: (String?) -> Unit,
    onCards: () -> Unit,
    onCalc: () -> Unit,
    onFlags: () -> Unit
) {
    val v = store.version
    val due = if (v >= 0) Course.flashcards.count { store.isDue(it.id) } else 0
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("🎯 Practice", fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text("Different ways to make it stick.", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(10.dp))
            HubCard("🃏", "Flashcards", "$due cards ready. Cards you know come back less often.", onCards)
            HubCard("🎲", "Mixed quiz", "10 random questions from everything", { onQuiz(null) })
            HubCard("🧮", "Calculation practice", "Ohm's law, power, series/parallel. Endless new questions.", onCalc)
            HubCard("🚩", "My flags", "${store.flags.size} things you flagged to check", onFlags)
            Spacer(Modifier.height(10.dp))
            Text("Quiz one section", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(Course.modules, key = { it.id }) { m ->
            val best = if (v >= 0) store.bestQuiz(m.id) else -1
            HubCard(m.emoji, m.title, if (best >= 0) "Best score: $best%" else "Not tried yet", { onQuiz(m.id) })
        }
    }
}

@Composable
fun QuizScreen(store: Store, moduleId: String?, onBack: () -> Unit) {
    var round by remember { mutableIntStateOf(0) }
    val questions: List<Quiz> = remember(moduleId, round) { Course.quizzes(moduleId).shuffled().take(10) }
    var idx by remember(round) { mutableIntStateOf(0) }
    var score by remember(round) { mutableIntStateOf(0) }
    var picked by remember(round, idx) { mutableStateOf<Int?>(null) }
    var saved by remember(round) { mutableStateOf(false) }
    val title = moduleId?.let { Course.module(it)?.title } ?: "Mixed quiz"

    Column(Modifier.fillMaxSize()) {
        Header("🎯 $title", onBack)
        if (questions.isEmpty()) {
            Text("No questions here yet.", modifier = Modifier.padding(16.dp))
            return@Column
        }
        if (idx >= questions.size) {
            LaunchedEffect(round) {
                if (!saved) {
                    store.recordQuiz(moduleId ?: "mixed", score, questions.size)
                    saved = true
                }
            }
            Column(
                Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(if (score * 10 >= questions.size * 8) "🏆" else "💪", fontSize = 64.sp)
                Text("$score / ${questions.size}", fontSize = 40.sp, fontWeight = FontWeight.Black)
                Text(
                    if (score == questions.size) "Perfect!" else "Every wrong answer is something you just learned.",
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Button(onClick = { round++ }, modifier = Modifier.fillMaxWidth()) { Text("Try another round") }
                OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Done") }
            }
            return@Column
        }
        val q = questions[idx]
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("Question ${idx + 1} of ${questions.size} · Score $score", style = MaterialTheme.typography.labelLarge)
            Text(q.q, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 10.dp))
            q.options.forEachIndexed { i, o ->
                val p = picked
                val bg = when {
                    p == null -> MaterialTheme.colorScheme.surfaceVariant
                    i == q.answer -> Palette.Good.copy(alpha = 0.35f)
                    i == p -> Palette.Bad.copy(alpha = 0.30f)
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                Text(
                    o,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bg)
                        .clickable(enabled = picked == null) {
                            picked = i
                            if (i == q.answer) score++
                        }
                        .padding(14.dp)
                )
            }
            val p = picked
            if (p != null) {
                Text(
                    (if (p == q.answer) "✅ Correct! " else "❌ Not this time. ") + q.why,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        if (picked != null) {
            Button(onClick = { idx++ }, modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                Text(if (idx == questions.lastIndex) "See my score" else "Next question →")
            }
        }
    }
}

@Composable
fun FlashcardScreen(store: Store, onBack: () -> Unit) {
    var practiceAll by remember { mutableStateOf(false) }
    var round by remember { mutableIntStateOf(0) }
    val deck = remember(practiceAll, round) {
        val d = Course.flashcards.filter { practiceAll || store.isDue(it.id) }
        d.shuffled()
    }
    var idx by remember(practiceAll, round) { mutableIntStateOf(0) }
    var flipped by remember(practiceAll, round, idx) { mutableStateOf(false) }
    val v = store.version
    val mastered = if (v >= 0) Course.flashcards.count { store.box(it.id) >= 3 } else 0

    Column(Modifier.fillMaxSize()) {
        Header("🃏 Flashcards", onBack)
        Text(
            "Mastered: $mastered / ${Course.flashcards.size}",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        if (idx >= deck.size) {
            Column(
                Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("🎉", fontSize = 56.sp)
                Text(if (deck.isEmpty()) "All caught up for today!" else "Deck done!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Cards you got right come back in a few days. That's how it sticks long-term.", textAlign = TextAlign.Center)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { practiceAll = true; round++ }, modifier = Modifier.fillMaxWidth()) { Text("Practise all cards anyway") }
            }
            return@Column
        }
        val c = deck[idx]
        val mod = Course.module(c.moduleId)
        Column(Modifier.weight(1f).padding(16.dp), verticalArrangement = Arrangement.Center) {
            Text("${idx + 1} / ${deck.size}  ·  ${mod?.emoji ?: ""} ${mod?.title ?: ""}", style = MaterialTheme.typography.labelMedium)
            Box(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 260.dp)
                    .padding(vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (flipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { flipped = !flipped }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (flipped) c.back else c.front,
                    fontSize = if (flipped) 18.sp else 22.sp,
                    fontWeight = if (flipped) FontWeight.Normal else FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Text(if (flipped) "Did you know it?" else "Tap the card to flip it", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        if (flipped) {
            Row(Modifier.fillMaxWidth().padding(12.dp)) {
                Button(
                    onClick = { store.gradeCard(c.id, false); idx++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Palette.Bad),
                    modifier = Modifier.weight(1f)
                ) { Text("😕 Not yet") }
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = { store.gradeCard(c.id, true); idx++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Palette.Good),
                    modifier = Modifier.weight(1f)
                ) { Text("😎 Got it") }
            }
        }
    }
}

// ---------------- calculation practice ----------------

private data class Problem(val question: String, val answer: Double, val unit: String, val working: String)

private fun fmt(x: Double): String =
    if (abs(x - x.roundToLong()) < 1e-9) x.roundToLong().toString() else String.format(Locale.US, "%.2f", x)

private fun makeProblem(r: Random): Problem {
    return when (r.nextInt(8)) {
        0 -> {
            val v = listOf(12, 24, 48, 230).random(r)
            val res = listOf(2, 4, 6, 8, 10, 23, 46, 115).random(r)
            val i = v.toDouble() / res
            Problem("A $v V supply is connected across a $res Ω resistor. What current flows?", i, "A",
                "I = V ÷ R = $v ÷ $res = ${fmt(i)} A")
        }
        1 -> {
            val i = listOf(0.5, 1.0, 2.0, 3.0, 5.0, 10.0).random(r)
            val res = listOf(4, 6, 10, 12, 20, 23).random(r)
            val v = i * res
            Problem("${fmt(i)} A flows through a $res Ω resistance. What is the voltage across it?", v, "V",
                "V = I × R = ${fmt(i)} × $res = ${fmt(v)} V")
        }
        2 -> {
            val v = listOf(12, 24, 230).random(r)
            val i = listOf(0.5, 2.0, 4.0, 5.0, 10.0).random(r)
            val res = v / i
            Problem("A $v V supply pushes ${fmt(i)} A through a load. What is the load's resistance?", res, "Ω",
                "R = V ÷ I = $v ÷ ${fmt(i)} = ${fmt(res)} Ω")
        }
        3 -> {
            val (name, w) = listOf("kettle" to 2400, "heater" to 2000, "toaster" to 920, "hair dryer" to 1840, "microwave" to 1150).random(r)
            val i = w / 230.0
            Problem("A $w W $name runs on 230 V. Roughly what current does it draw?", i, "A",
                "I = P ÷ V = $w ÷ 230 = ${fmt(i)} A")
        }
        4 -> {
            val v = listOf(12, 24, 230).random(r)
            val i = listOf(0.5, 1.0, 2.0, 4.0, 8.0).random(r)
            val pw = v * i
            Problem("A device on $v V draws ${fmt(i)} A. What power does it use?", pw, "W",
                "P = V × I = $v × ${fmt(i)} = ${fmt(pw)} W")
        }
        5 -> {
            val a = r.nextInt(1, 10) * 5
            val b = r.nextInt(1, 10) * 5
            val c = r.nextInt(1, 10) * 5
            val t = (a + b + c).toDouble()
            Problem("Three resistors are in SERIES: $a Ω, $b Ω and $c Ω. What is the total resistance?", t, "Ω",
                "Series: just add them. $a + $b + $c = ${fmt(t)} Ω")
        }
        6 -> {
            val pair = listOf(10 to 10, 20 to 20, 6 to 3, 12 to 6, 30 to 60, 40 to 10, 100 to 25).random(r)
            val t = pair.first.toDouble() * pair.second / (pair.first + pair.second)
            Problem("Two resistors are in PARALLEL: ${pair.first} Ω and ${pair.second} Ω. What is the total resistance?", t, "Ω",
                "Parallel (two resistors): (R1 × R2) ÷ (R1 + R2) = (${pair.first} × ${pair.second}) ÷ (${pair.first} + ${pair.second}) = ${fmt(t)} Ω. Always LESS than the smallest one.")
        }
        else -> {
            val kw = listOf(1.0, 1.5, 2.0, 2.4, 3.0).random(r)
            val h = listOf(2, 3, 4, 5, 8).random(r)
            val e = kw * h
            Problem("A ${fmt(kw)} kW heater runs for $h hours. How much energy does it use?", e, "kWh",
                "Energy = power × time = ${fmt(kw)} kW × $h h = ${fmt(e)} kWh")
        }
    }
}

@Composable
fun CalcScreen(store: Store, onBack: () -> Unit) {
    var seed by remember { mutableIntStateOf(Random.nextInt()) }
    val prob = remember(seed) { makeProblem(Random(seed)) }
    var input by remember(seed) { mutableStateOf("") }
    var result by remember(seed) { mutableStateOf<Boolean?>(null) }
    var streak by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        Header("🧮 Calculation practice", onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Text("Correct in a row: $streak", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Text(prob.question, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Your answer (${prob.unit})") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val x = input.replace(",", ".").trim().toDoubleOrNull()
                val ok = x != null && abs(x - prob.answer) <= maxOf(0.02 * abs(prob.answer), 0.05)
                result = ok
                if (ok) {
                    streak++
                    store.addXp(3)
                    store.touchStreak()
                } else {
                    streak = 0
                }
            }, enabled = result == null && input.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Check") }
            val res = result
            if (res != null) {
                Text(
                    if (res) "✅ Correct!" else "❌ Answer: ${fmt(prob.answer)} ${prob.unit}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Text("Working: ${prob.working}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 6.dp))
                Button(onClick = { seed = Random.nextInt() }, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) { Text("Next question →") }
            }
            Spacer(Modifier.height(16.dp))
            Callout(
                "📐", "Cheat sheet",
                "V = I × R    (volts = amps × ohms)\nI = V ÷ R\nR = V ÷ I\nP = V × I    (watts = volts × amps)\nSeries: R total = R1 + R2 + R3\nParallel (2): R total = (R1 × R2) ÷ (R1 + R2)\nEnergy (kWh) = kW × hours",
                Palette.Neutral
            )
        }
    }
}

// ---------------- progress, flags, about ----------------

@Composable
fun ProgressScreen(store: Store, onLesson: (String) -> Unit, onFlags: () -> Unit, onAbout: () -> Unit) {
    val v = store.version
    val df = remember { SimpleDateFormat("d MMM yyyy", Locale.getDefault()) }
    val doneLessons = Course.modules.flatMap { it.lessons }.filter { it.id in store.done }
        .sortedByDescending { store.doneAt(it.id) }
    val mastered = if (v >= 0) Course.flashcards.count { store.box(it.id) >= 3 } else 0
    var confirmReset by remember { mutableStateOf(false) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("📈 Your progress", fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text("Saved on this phone automatically.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                StatBox("⭐ ${store.xp}", "XP", Modifier.weight(1f))
                StatBox("🔥 ${store.displayStreak()}", "streak", Modifier.weight(1f))
                StatBox("${store.done.size}/${Course.totalLessons}", "lessons", Modifier.weight(1f))
            }
            Row(Modifier.fillMaxWidth()) {
                StatBox("${store.quizzesTaken()}", "quizzes", Modifier.weight(1f))
                StatBox("$mastered", "cards mastered", Modifier.weight(1f))
                StatBox("${store.flags.size}", "flags", Modifier.weight(1f).clickable { onFlags() })
            }
            Spacer(Modifier.height(14.dp))
            Text("By section", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(Course.modules, key = { "m_" + it.id }) { m ->
            val n = m.lessons.count { it.id in store.done }
            val best = if (v >= 0) store.bestQuiz(m.id) else -1
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(m.emoji, fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text(m.title, modifier = Modifier.weight(1f))
                Text("$n/${m.lessons.size}" + if (best >= 0) "  ·  quiz $best%" else "", style = MaterialTheme.typography.labelMedium)
            }
        }
        item {
            Spacer(Modifier.height(14.dp))
            Text("Completed lessons (tap to review)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            if (doneLessons.isEmpty()) Text("Nothing yet. Your first lesson is waiting in the Learn tab!", modifier = Modifier.padding(vertical = 8.dp))
        }
        items(doneLessons, key = { "d_" + it.id }) { l ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 3.dp).clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant).clickable { onLesson(l.id) }.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(l.emoji)
                Spacer(Modifier.width(8.dp))
                Text(l.title, modifier = Modifier.weight(1f))
                val t = store.doneAt(l.id)
                if (t > 0) Text(df.format(Date(t)), style = MaterialTheme.typography.labelSmall)
            }
        }
        item {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onFlags, modifier = Modifier.fillMaxWidth()) { Text("🚩 My flags") }
            OutlinedButton(onClick = onAbout, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) { Text("ℹ️ About & disclaimer") }
            TextButton(onClick = { confirmReset = true }, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                Text("Reset all progress", color = Palette.Bad)
            }
        }
    }
    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            title = { Text("Reset everything?") },
            text = { Text("This wipes your XP, streak, completed lessons, flashcard progress and flags. It can't be undone.") },
            confirmButton = {
                Button(onClick = { store.resetAll(); confirmReset = false }, colors = ButtonDefaults.buttonColors(containerColor = Palette.Bad)) { Text("Reset") }
            },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun FlagsScreen(store: Store, onBack: () -> Unit) {
    val clip = LocalClipboardManager.current
    val df = remember { SimpleDateFormat("d MMM yyyy", Locale.getDefault()) }
    var copied by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Header("🚩 My flags", onBack)
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            item {
                Text(
                    "Things you thought might be wrong or confusing. Show these to your TAFE lecturer or supervisor. Use the 🚩 button at the top of any lesson page to add one.",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (store.flags.isNotEmpty()) {
                    Button(onClick = {
                        val text = store.flags.joinToString("\n\n") { "• ${it.where}\n  ${it.note.ifBlank { "(no note)" }}" }
                        clip.setText(AnnotatedString("SparkStart – things to check:\n\n$text"))
                        copied = true
                    }, modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                        Text(if (copied) "Copied ✓ (paste into a message)" else "📋 Copy all to send to someone")
                    }
                } else {
                    Text("No flags yet.", modifier = Modifier.padding(vertical = 16.dp))
                }
            }
            items(store.flags.reversed(), key = { it.time }) { f ->
                Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(f.where, fontWeight = FontWeight.Bold)
                        if (f.note.isNotBlank()) Text(f.note, modifier = Modifier.padding(top = 4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(df.format(Date(f.time)), style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
                            TextButton(onClick = { store.removeFlag(f) }) { Text("Remove") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen(store: Store, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Header("ℹ️ About SparkStart", onBack)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
            Callout(
                "⚠️", "Read this first",
                "SparkStart is a free study aid to give you a head start. It is NOT the Wiring Rules and does not replace your TAFE/RTO training, your supervisor, your site procedures, or the actual standards.\n\n" +
                    "The content was written in plain English to explain ideas. The app could not check every clause number or limit against the actual text of AS/NZS 3000, because that's a paid, copyrighted document. Anything tagged 📘 or 🧑‍🔧 must be confirmed in your own copy of the standard or on site.\n\n" +
                    "Apprentices in WA must hold an electrical training licence and work under supervision. Never work live.",
                Palette.Bad
            )
            Callout(
                "📘", "Which edition?",
                "Written against AS/NZS 3000:2018 (the Wiring Rules) including Amendments 1, 2 and 3 (May 2023). In WA you also need the WA Electrical Requirements (WAER), which is free from the WA government website.",
                Palette.Neutral
            )
            Callout(
                "🔒", "Privacy",
                "No account, no ads, no tracking. The app doesn't even have permission to use the internet. Your progress is stored only on this phone.",
                Palette.Good
            )
            Callout(
                "🚩", "Found a mistake?",
                "Tap 🚩 on the lesson page to save a note. You currently have ${store.flags.size} flag(s). You can copy them all from Progress → My flags.",
                Palette.Current
            )
            Spacer(Modifier.height(12.dp))
            Text("Version: free & open source. Built automatically on GitHub.", style = MaterialTheme.typography.labelMedium)
        }
    }
}
