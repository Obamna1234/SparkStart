package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.ui.Palette
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.math.sin

private data class MeterResult(val display: String, val msg: String, val danger: Boolean = false, val good: Boolean = false)

private fun meterRead(mode: Int, target: Int): MeterResult {
    // modes: 0 OFF, 1 V~, 2 V⎓, 3 Ω, 4 continuity, 5 A
    // targets: 0 live power point, 1 AA battery, 2 good fuse, 3 blown fuse, 4 globe (out of circuit), 5 isolated circuit A–N
    if (mode == 0) return MeterResult("----", "Pick a mode on the dial first.")
    return when (mode) {
        1 -> when (target) {
            0 -> MeterResult("230 V", "Right mode for mains. Only use a meter that's CAT rated for the job, with leads in good condition.", good = true)
            1 -> MeterResult("0.0 V", "AC mode can't read a battery's DC. Switch to V⎓ (DC volts).")
            5 -> MeterResult("0.0 V", "Reads 0 V. But only TRUST that if you proved your tester on a known live source before AND after testing.")
            else -> MeterResult("0.0 V", "No voltage: it isn't connected to anything live.")
        }
        2 -> when (target) {
            1 -> MeterResult("1.5 V", "Right mode for a battery.", good = true)
            0 -> MeterResult("0.7 V?", "WRONG MODE: DC volts on AC mains gives a misleading reading. You might think it's dead when it's 230 V!", danger = true)
            else -> MeterResult("0.0 V", "No voltage here.")
        }
        3 -> when (target) {
            0 -> MeterResult("ERR", "NEVER measure resistance on a live circuit. It can wreck the meter and hurt you. Isolate first.", danger = true)
            1 -> MeterResult("???", "Don't measure resistance across a battery. It's a power source, so the reading is meaningless.")
            2 -> MeterResult("0.1 Ω", "Near zero ohms: the fuse is intact (a good path).", good = true)
            3 -> MeterResult("OL", "OL = open loop (no path at all). This fuse is blown.", good = true)
            4 -> MeterResult("≈ 60 Ω", "A cold filament reads a low-ish resistance, so it isn't broken. (It's much higher when hot and glowing.)", good = true)
            else -> MeterResult("OL", "No path between A and N with loads off. But a multimeter only uses a tiny voltage. Proper insulation testing needs a 500 V insulation tester.")
        }
        4 -> when (target) {
            0 -> MeterResult("ERR", "Continuity mode on a live circuit is dangerous. Isolate first!", danger = true)
            2 -> MeterResult("BEEP 🔊", "Beep = continuous path. Fuse is good.", good = true)
            3 -> MeterResult("OL", "No beep: the path is broken. Fuse is blown.", good = true)
            4 -> MeterResult("60 Ω", "May not beep. Most beepers only beep below a few tens of ohms. Use Ω mode to see the real value.")
            1 -> MeterResult("???", "Not for batteries.")
            else -> MeterResult("OL", "No continuity between A and N with loads off.")
        }
        else -> when (target) {
            0 -> MeterResult("💥", "EXTREME DANGER: in amps mode the meter is almost a dead short. Across 230 V it can explode in your hand (arc flash). Current is measured IN SERIES, or better, with a clamp meter.", danger = true)
            1 -> MeterResult("💥", "That short-circuits the battery through the meter. Don't.", danger = true)
            else -> MeterResult("0.00 A", "Nothing is flowing, so there's nothing to measure.")
        }
    }
}

@Composable
internal fun MultimeterSim() {
    var mode by remember { mutableIntStateOf(0) }
    var target by remember { mutableIntStateOf(0) }
    val res = meterRead(mode, target)
    Text("1. Turn the dial:", style = MaterialTheme.typography.labelLarge)
    PillRow(listOf("OFF", "V~ AC volts", "V⎓ DC volts", "Ω ohms", "🔊 continuity", "A amps"), mode) { mode = it }
    Text("2. Put the probes on:", style = MaterialTheme.typography.labelLarge)
    PillRow(listOf("Live power point", "AA battery", "Good fuse", "Blown fuse", "Light globe", "Isolated circuit A–N"), target) { target = it }
    Box(
        Modifier.fillMaxWidth().padding(vertical = 8.dp).clip(RoundedCornerShape(14.dp))
            .background(if (res.danger) Palette.Bad else Color(0xFF263238)).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(res.display, color = Color(0xFFB2FF59), fontSize = 40.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
    }
    Explain(res.msg, if (res.danger) Palette.Bad else Color.Unspecified)
}

@Composable
internal fun InsulationSim() {
    var cable by remember { mutableIntStateOf(0) }
    var tested by remember(cable) { mutableStateOf(false) }
    val value = when (cable) {
        0 -> 999f
        1 -> 0.6f
        else -> 0.02f
    }
    val target = if (tested) ((log10(value + 0.01f) + 2f) / 5f).coerceIn(0f, 1f) else 0f
    val needle by animateFloatAsState(target, tween(1400), label = "needle")
    val ink = ink()
    PillRow(listOf("New cable", "Damp / water got in", "Nail through cable"), cable) { cable = it }
    Canvas(Modifier.fillMaxWidth().height(160.dp)) {
        val c = Offset(size.width / 2, size.height * 0.92f)
        val r = size.height * 0.8f
        drawArc(ink.copy(alpha = 0.25f), 180f, 180f, false, Offset(c.x - r, c.y - r), Size(r * 2, r * 2), style = Stroke(12.dp.toPx()))
        // pass zone from 1 MΩ upward: 1 MΩ maps to (0+2)/5 = 0.4
        drawArc(Palette.Good.copy(alpha = 0.6f), 180f + 180f * 0.4f, 180f * 0.6f, false, Offset(c.x - r, c.y - r), Size(r * 2, r * 2), style = Stroke(12.dp.toPx()))
        drawArc(Palette.Bad.copy(alpha = 0.6f), 180f, 180f * 0.4f, false, Offset(c.x - r, c.y - r), Size(r * 2, r * 2), style = Stroke(12.dp.toPx()))
        val ang = Math.toRadians((180.0 + 180.0 * needle))
        val tip = Offset(c.x + (r * cos(ang)).toFloat(), c.y + (r * sin(ang)).toFloat())
        drawLine(ink, c, tip, 3.dp.toPx(), cap = StrokeCap.Round)
        drawCircle(ink, 6.dp.toPx(), c)
        label("0.01", c.x - r, c.y + 2.dp.toPx(), ink, 9f)
        label("1 MΩ", c.x + (r * cos(Math.toRadians(252.0))).toFloat(), c.y + (r * sin(Math.toRadians(252.0))).toFloat() - 10.dp.toPx(), ink, 10f)
        label("1000 MΩ", c.x + r, c.y + 2.dp.toPx(), ink, 9f, Paint.Align.RIGHT)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { tested = true }) { Text("Press TEST (500 V DC)") }
    }
    if (tested) {
        val pass = value >= 1f
        Text(
            if (value > 500f) "Reading: >999 MΩ ✅ PASS" else "Reading: ${f2(value)} MΩ " + if (pass) "✅ PASS" else "❌ FAIL",
            fontWeight = FontWeight.Bold,
            color = if (pass) Palette.Good else Palette.Bad,
            style = MaterialTheme.typography.titleMedium
        )
    }
    Explain(
        when (cable) {
            0 -> "Good insulation lets almost NO current leak, so the resistance is huge (hundreds of megohms)."
            1 -> "Moisture lets a little current leak through the insulation. The reading drops below the 1 MΩ minimum, so find and fix it."
            else -> "A nail has pierced the insulation and touched the conductor. Near-zero megohms is a dead short to earth."
        }
    )
    Explain("Before you press test: circuit ISOLATED and proved dead, and sensitive electronics (dimmers, surge protectors, smart devices, appliances) disconnected or they can be damaged. The tester makes high voltage, so don't touch the leads while testing.")
}

@Composable
internal fun ClampSim() {
    var mode by remember { mutableIntStateOf(0) }
    val ink = ink()
    PillRow(listOf("Clamp the ACTIVE only", "Clamp the whole cable (A+N)", "Whole cable, 40 mA leaking"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val y1 = size.height * 0.42f
        val y2 = size.height * 0.58f
        drawLine(Palette.Active, Offset(0f, y1), Offset(size.width, y1), 6.dp.toPx())
        drawLine(Palette.Neutral, Offset(0f, y2), Offset(size.width, y2), 6.dp.toPx())
        val cx = size.width / 2
        val (top, bot) = if (mode == 0) (y1 - 18.dp.toPx()) to (y1 + 10.dp.toPx()) else (y1 - 18.dp.toPx()) to (y2 + 18.dp.toPx())
        drawOval(Color(0xFFFFC107), Offset(cx - 26.dp.toPx(), top), Size(52.dp.toPx(), bot - top), style = Stroke(6.dp.toPx()))
        label("A", 10.dp.toPx(), y1 - 8.dp.toPx(), Palette.Active, 11f, Paint.Align.LEFT)
        label("N", 10.dp.toPx(), y2 + 18.dp.toPx(), Palette.Neutral, 11f, Paint.Align.LEFT)
        label("→ 10 A out", size.width * 0.8f, y1 - 10.dp.toPx(), ink, 10f)
        label("← back", size.width * 0.8f, y2 + 20.dp.toPx(), ink, 10f)
    }
    val reading = when (mode) {
        0 -> "10.0 A"
        1 -> "0.00 A"
        else -> "0.04 A"
    }
    Text("Clamp reads: $reading", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
    Explain(
        when (mode) {
            0 -> "A clamp meter measures the magnetic field around a conductor. Clamp ONE conductor to read the load current. No need to disconnect anything."
            1 -> "Clamp both active AND neutral and the fields cancel out (current out = current back), so it reads about zero. Common beginner mistake!"
            else -> "…unless some current is leaking to earth. Then out ≠ back, and the clamp shows the leakage. That's the exact same idea an RCD uses. (Leakage clamps are made for this.)"
        }
    )
}

@Composable
internal fun LoopImpedanceSim() {
    var zs by remember { mutableFloatStateOf(0.8f) }
    val amps = 230f / zs
    val need = 200f // 20 A C-curve: about 10 x In for an instant (magnetic) trip
    val ok = amps >= need
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(70.dp)) {
        val maxA = 1000f
        drawRect(ink.copy(alpha = 0.15f), Offset(0f, 18.dp.toPx()), Size(size.width, 24.dp.toPx()))
        drawRect(if (ok) Palette.Good else Palette.Bad, Offset(0f, 18.dp.toPx()), Size(size.width * (amps / maxA).coerceAtMost(1f), 24.dp.toPx()))
        val nx = size.width * need / maxA
        drawLine(ink, Offset(nx, 8.dp.toPx()), Offset(nx, 52.dp.toPx()), 2.dp.toPx())
        label("needed ≈ 200 A", nx + 4.dp.toPx(), 62.dp.toPx(), ink, 10f, Paint.Align.LEFT)
    }
    Text("Fault current = 230 V ÷ ${f2(zs)} Ω = ${amps.roundToInt()} A", fontWeight = FontWeight.Bold)
    LabeledSlider("Earth fault-loop impedance Zs: ${f2(zs)} Ω", zs, 0.2f..4f) { zs = it }
    Explain(
        if (ok) "Low loop impedance → big fault current → a 20 A C-curve breaker trips magnetically, almost instantly. ✅"
        else "Loop impedance too high → fault current too small for an instant trip. The metal could stay live for too long. ❌ Causes: long run, small cable, loose or high-resistance earth joint.",
        if (ok) Color.Unspecified else Palette.Bad
    )
    Explain("This uses a simple 10 × rule for a C-curve breaker to show the idea. The real maximum Zs values for each breaker are in a table in Section 8 of the Wiring Rules. Use those, not this slider.")
}

@Composable
internal fun VoltageDropSim() {
    var length by remember { mutableFloatStateOf(30f) }
    var amps by remember { mutableFloatStateOf(16f) }
    var sizeIdx by remember { mutableIntStateOf(1) }
    val sizes = listOf("1.5 mm²", "2.5 mm²", "4 mm²", "6 mm²", "10 mm²")
    // rough classroom mV/A·m values for single-phase copper (illustration only)
    val vc = listOf(29f, 18f, 11f, 7.3f, 4.4f)[sizeIdx]
    val vd = length * amps * vc / 1000f
    val pct = vd / 230f * 100f
    val ok = pct <= 5f
    val ink = ink()
    PillRow(sizes, sizeIdx) { sizeIdx = it }
    Canvas(Modifier.fillMaxWidth().height(60.dp)) {
        val maxPct = 10f
        drawRect(ink.copy(alpha = 0.15f), Offset(0f, 14.dp.toPx()), Size(this.size.width, 22.dp.toPx()))
        drawRect(if (ok) Palette.Good else Palette.Bad, Offset(0f, 14.dp.toPx()), Size(this.size.width * (pct / maxPct).coerceAtMost(1f), 22.dp.toPx()))
        val lx = this.size.width * 5f / maxPct
        drawLine(ink, Offset(lx, 6.dp.toPx()), Offset(lx, 44.dp.toPx()), 2.dp.toPx())
        label("5% limit", lx + 4.dp.toPx(), 54.dp.toPx(), ink, 10f, Paint.Align.LEFT)
    }
    Text("Drop ≈ ${f1(vd)} V (${f1(pct)}%) → ${f1(230f - vd)} V at the end", fontWeight = FontWeight.Bold)
    LabeledSlider("Cable length: ${length.roundToInt()} m", length, 5f..100f) { length = it }
    LabeledSlider("Current: ${amps.roundToInt()} A", amps, 1f..32f) { amps = it }
    Explain("Longer cable, more current, or thinner cable = more voltage lost along the way. Fix a big drop by using a bigger cable (or a shorter run).")
    Explain("The mV/A·m numbers here are ROUGH classroom values just to show the idea. Real values come from AS/NZS 3008.1.1 tables and depend on cable type, temperature and installation method.")
}
