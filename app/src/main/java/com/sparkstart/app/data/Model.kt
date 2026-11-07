package com.sparkstart.app.data

/** Every animated / interactive diagram in the app. */
enum class Visual {
    CIRCUIT, WATER, OHMS, SERIES_PARALLEL, AC_WAVE, BODY_CURRENT, SWITCH_ACTIVE,
    MEN_FAULT, RCD, BREAKER, MULTIMETER, INSULATION, CLAMP, LOOP_IMPEDANCE,
    VOLTAGE_DROP, THREE_PHASE, INDUCTION_MOTOR, DC_MOTOR, TRANSFORMER, STAR_DELTA,
    PUMP_SHARED, PILOT_CORE, NER, BACKFEED, HALF_SPLIT
}

enum class Level(val label: String, val emoji: String) {
    BEGINNER("Beginner", "🟢"),
    INTERMEDIATE("Intermediate", "🟡"),
    ADVANCED("Advanced", "🔴")
}

enum class Group { CORE, INDUSTRY }

sealed interface Block

/** Plain paragraph. */
data class P(val text: String) : Block
data class Bullets(val items: List<String>) : Block
data class Tip(val text: String) : Block
/** Hazard: what can hurt you. */
data class Hazard(val text: String) : Block
/** "Why you can't do it that way". */
data class WhyNot(val text: String) : Block
/** How to find faults. */
data class FaultFind(val steps: List<String>) : Block
/** A reference to the Wiring Rules (or another standard). Always tagged "check it". */
data class Std(val ref: String, val text: String) : Block
/** Site-specific: confirm with your supervisor / site procedure. */
data class Ask(val text: String) : Block
data class Vis(val visual: Visual, val caption: String = "") : Block
/** Tap to expand: the more in-depth explanation. */
data class Deep(val text: String) : Block
data class Quiz(val q: String, val options: List<String>, val answer: Int, val why: String) : Block
/** Tap the steps in the right order. */
data class Order(val prompt: String, val steps: List<String>) : Block

data class Page(val title: String, val blocks: List<Block>)

data class Lesson(
    val id: String,
    val title: String,
    val emoji: String,
    val minutes: Int,
    val level: Level,
    val pages: List<Page>
)

data class Module(
    val id: String,
    val title: String,
    val emoji: String,
    val blurb: String,
    val color: Long,
    val group: Group,
    val lessons: List<Lesson>
)

data class Flashcard(val id: String, val front: String, val back: String, val moduleId: String)

// ---- tiny helpers so the content files read nicely ----
fun page(title: String, vararg blocks: Block) = Page(title, blocks.toList())
fun lesson(id: String, title: String, emoji: String, minutes: Int, level: Level, vararg pages: Page) =
    Lesson(id, title, emoji, minutes, level, pages.toList())
fun bullets(vararg items: String) = Bullets(items.toList())
fun faults(vararg steps: String) = FaultFind(steps.toList())
fun order(prompt: String, vararg steps: String) = Order(prompt, steps.toList())
fun quiz(q: String, answer: Int, why: String, vararg options: String) = Quiz(q, options.toList(), answer, why)
