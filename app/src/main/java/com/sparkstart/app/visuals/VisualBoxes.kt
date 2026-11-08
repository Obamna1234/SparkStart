package com.sparkstart.app.visuals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.ui.Palette
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

private val LGreen = Color(0xFF2E7D32)
private val LBlue = Color(0xFF1565C0)
private val LRed = Color(0xFFC62828)
private val LAmber = Color(0xFFFF8F00)
private val LWhite = Color(0xFFF5F5F5)
private val DoorBlue = Color(0xFF1E4FA8)

private data class LampSpec(val text: String, val color: Color, val on: Boolean)

/** Grid of small buttons, 3 per row. */
@Composable
private fun BtnGrid(labels: List<String>, onClick: (Int) -> Unit) {
    labels.chunked(3).forEachIndexed { r, row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            row.forEachIndexed { c, l ->
                OutlinedButton(
                    onClick = { onClick(r * 3 + c) },
                    modifier = Modifier.weight(1f).padding(vertical = 1.dp)
                ) { Text(l, fontSize = 11.sp, textAlign = TextAlign.Center, maxLines = 2) }
            }
            repeat(3 - row.size) { Text("", Modifier.weight(1f)) }
        }
    }
}

private fun DrawScope.doorLamps(title: String, groups: List<Pair<String, List<LampSpec>>>) {
    drawRoundRect(DoorBlue, Offset.Zero, size, CornerRadius(10.dp.toPx()))
    // hazard stripes down the sides
    val sw = 8.dp.toPx()
    var y = 0f
    var red = true
    while (y < size.height) {
        drawRect(if (red) Color(0xFFD32F2F) else Color.White, Offset(0f, y), Size(sw, 10.dp.toPx()))
        drawRect(if (red) Color(0xFFD32F2F) else Color.White, Offset(size.width - sw, y), Size(sw, 10.dp.toPx()))
        y += 10.dp.toPx(); red = !red
    }
    label(title, size.width / 2, 16.dp.toPx(), Color.White, 11f)
    val rowH = (size.height - 24.dp.toPx()) / groups.size
    groups.forEachIndexed { gi, (gName, lamps) ->
        val top = 24.dp.toPx() + gi * rowH
        label(gName, size.width / 2, top + 10.dp.toPx(), Color(0xFFFFF59D), 10f)
        val n = lamps.size
        val step = (size.width - 2 * sw - 8.dp.toPx()) / n
        lamps.forEachIndexed { i, l ->
            val cx = sw + 4.dp.toPx() + step * (i + 0.5f)
            val cy = top + rowH * 0.52f
            val r = 9.dp.toPx()
            if (l.on) drawCircle(l.color.copy(alpha = 0.35f), r * 1.8f, Offset(cx, cy))
            drawCircle(if (l.on) l.color else l.color.copy(alpha = 0.25f), r, Offset(cx, cy))
            drawCircle(Color.Black, r, Offset(cx, cy), style = Stroke(1.5.dp.toPx()))
            label(l.text, cx, cy + r + 11.dp.toPx(), Color.White, 8f)
        }
    }
}

// ------------------------------------------------------------------ BOX DOOR (JF / mono-Flygt / fan)

@Composable
internal fun BoxDoorSim() {
    var box by remember { mutableIntStateOf(0) }
    PillRow(listOf("Jumbo + pump (JF)", "Mono + Flygt pump", "Dual fan"), box) { box = it }
    when (box) {
        0 -> JfDoor()
        1 -> MonoDoor()
        else -> FanDoor()
    }
}

@Composable
private fun JfDoor() {
    var main by remember { mutableStateOf(true) }
    var jPlug by remember { mutableStateOf(true) }
    var jRun by remember { mutableStateOf(false) }
    var jEl by remember { mutableStateOf(false) }
    var pPlug by remember { mutableStateOf(true) }
    var pRun by remember { mutableStateOf(false) }
    var pEl by remember { mutableStateOf(false) }
    var pOl by remember { mutableStateOf(false) }
    var msg by remember { mutableStateOf("Try starting the jumbo and the pump. Then cause some faults.") }
    val jPilot = main && jPlug
    val pPilot = main && pPlug
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        doorLamps(
            "1000 V JUMBO / PUMP STARTER (recreated)",
            listOf(
                "JUMBO" to listOf(
                    LampSpec("Power", LGreen, main), LampSpec("Pilot OK", LBlue, jPilot),
                    LampSpec("Run", LRed, jRun), LampSpec("E/L fault", LAmber, jEl)
                ),
                "PUMP" to listOf(
                    LampSpec("Power", LGreen, main), LampSpec("Pilot OK", LBlue, pPilot),
                    LampSpec("Run", LRed, pRun), LampSpec("E/L fault", LAmber, pEl), LampSpec("O/L fault", LAmber, pOl)
                )
            )
        )
    }
    BtnGrid(
        listOf(
            if (main) "Main isolator OFF" else "Main isolator ON",
            if (jPlug) "Unplug jumbo" else "Plug jumbo in",
            "Jumbo START (from rig)", "Jumbo STOP", "Jumbo E/L TEST", "Jumbo E/L RESET",
            if (pPlug) "Unplug pump" else "Plug pump in",
            "Pump START", "Pump STOP", "Pump E/L TEST", "Pump E/L RESET", "Pump jams (overload)", "Pump O/L RESET"
        )
    ) { i ->
        when (i) {
            0 -> { main = !main; if (!main) { jRun = false; pRun = false }
                msg = if (main) "Power is back. Every lamp needs its conditions met before anything runs." else "Main isolator off: everything drops out. Lock it here before you work on the box." }
            1 -> { jPlug = !jPlug; jRun = false
                msg = if (jPlug) "Plugged in: the pilot loop (pilot core → diode → earth) is complete, so Pilot Healthy comes on." else "Unplugged: the pilot loop is broken, so the pilot relay drops out and the jumbo contactor can't close. The receptacle should never be separated with power on. Isolate first." }
            2 -> { msg = when {
                    !main -> "No power."
                    !jPilot -> "Won't start: pilot not healthy. The pilot relay contact is in series with the start circuit."
                    jEl -> "Won't start: the E/L relay is tripped. Its contact is open in the control circuit. Find the cause, then reset."
                    else -> { jRun = true; "Jumbo contactor closed. The start signal came from the rig down the pilot (remote start)." }
                } }
            3 -> { jRun = false; msg = "Jumbo stopped." }
            4 -> { if (main) { jEl = true; jRun = false
                msg = "E/L TEST: about 24 V pushed through a 220 Ω resistor, with 1 wire passing through the toroid. 24 ÷ 220 ≈ 109 mA, which is MORE than the 100 mA setting, so the relay trips. That proves the toroid, relay and contactor all work." } }
            5 -> { jEl = false; msg = "E/L reset. With a real trip, find out WHY before you reset. Resetting onto a fault again and again is dangerous." }
            6 -> { pPlug = !pPlug; pRun = false
                msg = if (pPlug) "Pump plugged in: the pilot to the diode at the pump end is healthy." else "Pump unplugged: pump pilot drops out." }
            7 -> { msg = when {
                    !main -> "No power."
                    !pPilot -> "Won't start: pump pilot not healthy."
                    pEl -> "Won't start: pump E/L tripped."
                    pOl -> "Won't start: overload tripped. Its NC contact (95-96) is open in the control circuit."
                    else -> { pRun = true; "Pump running (MANUAL). In AUTO, the pump controller or floats decide when it runs." }
                } }
            8 -> { pRun = false; msg = "Pump stopped." }
            9 -> { if (main) { pEl = true; pRun = false
                msg = "Pump E/L TEST: about 24 V through a 680 Ω resistor. 24 ÷ 680 ≈ 35 mA, just over the 30 mA setting, so it trips." } }
            10 -> { pEl = false; msg = "Pump E/L reset." }
            11 -> { if (pRun) { pOl = true; pRun = false
                msg = "The pump drew too much current for too long (jammed impeller, mud, lost phase…). The overload heats up its model of the motor and trips, opening 95-96." } else msg = "Start the pump first." }
            12 -> { pOl = false; msg = "Overload reset (remote reset module). If it trips again straight away, stop and fault-find. Don't keep resetting." }
        }
    }
    Explain(msg)
}

@Composable
private fun MonoDoor() {
    var main by remember { mutableStateOf(true) }
    var auto by remember { mutableStateOf(false) }
    var mRun by remember { mutableStateOf(false) }
    var flow by remember { mutableStateOf(true) }
    var noFlow by remember { mutableStateOf(false) }
    var mOl by remember { mutableStateOf(false) }
    var fRun by remember { mutableStateOf(false) }
    var fOl by remember { mutableStateOf(false) }
    var fEl by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(0) }
    var msg by remember { mutableStateOf("The mono (positive-displacement) pump has a no-flow timer. Try running it with no flow.") }
    LaunchedEffect(mRun, flow) {
        timer = 0
        if (mRun && !flow) {
            for (s in 1..5) { delay(700); timer = s }
            noFlow = true; mRun = false
            msg = "NO FLOW FAULT. The pump ran but no flow was proven before the timer ran out, so the timer opened the run circuit. A mono pump running dry destroys its rubber stator fast. Find out why (suction, valve shut, blocked line), then press No Flow Reset."
        }
    }
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        doorLamps(
            "1 kV MONO / FLYGT PUMP STARTER (recreated)",
            listOf(
                "MONO" to listOf(
                    LampSpec("Power", LGreen, main), LampSpec("Run", LRed, mRun),
                    LampSpec("No flow", LAmber, noFlow), LampSpec("O/L fault", LAmber, mOl)
                ),
                "FLYGT" to listOf(
                    LampSpec("Power", LGreen, main), LampSpec("Pilot OK", LBlue, main),
                    LampSpec("Run", LRed, fRun), LampSpec("E/L fault", LAmber, fEl), LampSpec("O/L fault", LAmber, fOl)
                )
            )
        )
    }
    if (mRun && !flow) Text("No-flow timer: ${timer}/5 (sped up)", color = Palette.Bad, style = MaterialTheme.typography.labelLarge)
    BtnGrid(
        listOf(
            "Mono START", "Mono STOP", "No Flow RESET",
            if (flow) "Close the valve (no flow)" else "Open the valve (flow)",
            if (auto) "Selector → MAN" else "Selector → AUTO", "Mono jams (overload)",
            "Flygt START", "Flygt STOP", "Flygt E/L TEST", "Flygt E/L RESET", "Flygt O/L trip", "Flygt O/L RESET"
        )
    ) { i ->
        when (i) {
            0 -> msg = when {
                !main -> "No power."
                noFlow -> "Won't start: the no-flow fault is latched. Reset it first, after you fix the cause."
                mOl -> "Won't start: overload tripped."
                else -> { mRun = true; if (flow) "Mono running. Flow is proven, so the no-flow timer stays reset." else "Mono running… but there's no flow. Watch the timer." }
            }
            1 -> { mRun = false; msg = "Mono stopped." }
            2 -> { noFlow = false; msg = "No-flow reset." }
            3 -> { flow = !flow; msg = if (flow) "Valve open: flow returns." else "Valve shut: no flow." }
            4 -> { auto = !auto; msg = if (auto) "AUTO: the pump starts and stops from the level/flow instruments instead of the buttons (simplified here)." else "MANUAL: Start/Stop buttons, with the start held in by the contactor's own aux contact." }
            5 -> { if (mRun) { mOl = true; mRun = false; msg = "Mono overload trip. This overload is fed from three 100/5 CTs, so its dial is set in CT-secondary amps (see the overload calculator)." } else msg = "Start it first." }
            6 -> msg = when { fEl -> "E/L tripped."; fOl -> "O/L tripped."; else -> { fRun = true; "Flygt running." } }
            7 -> { fRun = false; msg = "Flygt stopped." }
            8 -> { fEl = true; fRun = false; msg = "Flygt E/L test: 24 V ÷ 680 Ω ≈ 35 mA, over 30 mA, so it trips." }
            9 -> { fEl = false; msg = "Flygt E/L reset." }
            10 -> { if (fRun) { fOl = true; fRun = false; msg = "Flygt overload trip." } else msg = "Start it first." }
            11 -> { fOl = false; msg = "Flygt O/L reset." }
        }
    }
    Explain(msg)
}

@Composable
private fun FanDoor() {
    var both by remember { mutableStateOf(true) }
    var f1 by remember { mutableStateOf(false) }
    var f2 by remember { mutableStateOf(false) }
    var ol1 by remember { mutableStateOf(false) }
    var ol2 by remember { mutableStateOf(false) }
    var delayAct by remember { mutableStateOf(false) }
    var startReq by remember { mutableIntStateOf(0) }
    var delayReq by remember { mutableIntStateOf(0) }
    var count by remember { mutableIntStateOf(0) }
    var msg by remember { mutableStateOf("Press START. Fan 2 is held back by a stage timer so the two big motors don't start at the same moment.") }
    LaunchedEffect(startReq) {
        if (startReq == 0) return@LaunchedEffect
        if (ol1) { msg = "Won't start: Fan 1 overload tripped."; return@LaunchedEffect }
        f1 = true
        msg = "Fan 1 started."
        if (both && !ol2) {
            for (s in 1..4) { delay(600); count = s }
            f2 = true
            msg = "Stage timer timed out, so Fan 2 starts. The two big starting currents are spread out in time."
        }
        count = 0
    }
    LaunchedEffect(delayReq) {
        if (delayReq == 0) return@LaunchedEffect
        delayAct = true
        msg = "Delay start activated: the delay-start timer is running. The fans will start by themselves when it times out."
        delay(3000)
        delayAct = false
        startReq++
    }
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        doorLamps(
            "1000 V DUAL 110 kW FAN STARTER (recreated)",
            listOf(
                "" to listOf(
                    LampSpec("Power", LGreen, true), LampSpec("F1 run", LRed, f1), LampSpec("F1 O/L", LAmber, ol1),
                    LampSpec("F2 run", LRed, f2), LampSpec("F2 O/L", LAmber, ol2), LampSpec("Delay", LWhite, delayAct)
                )
            )
        )
    }
    if (count > 0) Text("Fan 2 stage timer: $count/4 (sped up)", style = MaterialTheme.typography.labelLarge)
    BtnGrid(
        listOf(
            "START", "STOP (E-stop)", "DELAY START",
            if (both) "Selector → FAN 1" else "Selector → FAN 1 & 2",
            "Fan 1 overload trip", "Fan 2 overload trip", "Fan 1 O/L RESET", "Fan 2 O/L RESET"
        )
    ) { i ->
        when (i) {
            0 -> startReq++
            1 -> { f1 = false; f2 = false; delayAct = false; msg = "Both fans stopped. Underground, stopping a fan changes the ventilation, so follow your site's rules before you do it." }
            2 -> delayReq++
            3 -> { both = !both; if (!both) f2 = false; msg = if (both) "Selector on FAN 1 & 2." else "Selector on FAN 1 only: Fan 2's start path is open." }
            4 -> { if (f1) { ol1 = true; f1 = false; f2 = false; msg = "Fan 1 overload: on this drawing Fan 1's overload contact sits in the main control path, so BOTH fans drop out." } else msg = "Start first." }
            5 -> { if (f2) { ol2 = true; f2 = false; msg = "Fan 2 overload: only Fan 2 stops." } else msg = "Start first." }
            6 -> { ol1 = false; msg = "Fan 1 O/L reset." }
            7 -> { ol2 = false; msg = "Fan 2 O/L reset." }
        }
    }
    Explain(msg)
}

// ------------------------------------------------------------------ CT-fed overload calculator

private val labelTable = mapOf(5 to 3.2f, 8 to 6f, 18 to 13f, 20 to 14.5f, 37 to 26f, 45 to 32f, 55 to 40f, 75 to 54f, 90 to 65f, 110 to 79f, 132 to 95f)

@Composable
internal fun CtOverloadSim() {
    var kw by remember { mutableFloatStateOf(55f) }
    var pfEff by remember { mutableFloatStateOf(0.80f) }
    var ct by remember { mutableIntStateOf(1) }
    val ctPrim = listOf(0, 100, 200)[ct]
    val flc = kw * 1000f / (sqrt(3f) * 1000f * pfEff)
    val k = kw.toInt()
    val lbl = labelTable[k]
    val dial = if (ctPrim == 0) flc else flc * 5f / ctPrim
    val ink = ink()
    LabeledSlider("Motor size: ${k} kW at 1000 V", kw, 5f..132f) { kw = it.toInt().toFloat() }
    LabeledSlider("Power factor × efficiency: ${f2(pfEff)}", pfEff, 0.65f..0.95f) { pfEff = it }
    PillRow(listOf("Overload direct in line", "Via 100/5 CTs", "Via 200/5 CTs"), ct) { ct = it }
    Canvas(Modifier.fillMaxWidth().height(120.dp)) {
        val w = size.width
        // motor cable through CT -> overload
        val y = size.height * 0.35f
        drawLine(Palette.Active, Offset(0f, y), Offset(w, y), 6.dp.toPx())
        label("motor current ≈ ${f1(flc)} A", w * 0.25f, y - 10.dp.toPx(), ink, 11f)
        if (ctPrim > 0) {
            drawOval(Color(0xFF424242), Offset(w * 0.5f - 14.dp.toPx(), y - 22.dp.toPx()), Size(28.dp.toPx(), 44.dp.toPx()), style = Stroke(6.dp.toPx()))
            label("CT $ctPrim/5", w * 0.5f, y + 36.dp.toPx(), ink, 10f)
            drawLine(Palette.Current, Offset(w * 0.5f, y + 22.dp.toPx()), Offset(w * 0.75f, size.height * 0.8f), 3.dp.toPx())
        } else {
            drawLine(Palette.Current, Offset(w * 0.5f, y), Offset(w * 0.75f, size.height * 0.8f), 3.dp.toPx())
        }
        box(Offset(w * 0.65f, size.height * 0.62f), Size(w * 0.3f, size.height * 0.34f), Color(0xFFECEFF1), ink)
        label("overload dial", w * 0.8f, size.height * 0.75f, Color.Black, 10f)
        label("${f2(dial)} A", w * 0.8f, size.height * 0.92f, Palette.Bad, 13f)
    }
    val tableTxt = if (lbl != null) "The settings label on the door says ${k} kW at 1 kV → ${lbl} A. Our maths gives ${f1(flc)} A." else "This size isn't on the door label. Use the motor NAMEPLATE full-load current."
    val ctTxt = if (ctPrim == 0) "No CT: set the overload to the motor FLC, ${f1(flc)} A."
    else "With $ctPrim/5 CTs, the overload only sees 5 A when the motor draws $ctPrim A. So divide: ${f1(flc)} × 5 ÷ $ctPrim = ${f2(dial)} A on the dial." +
        (if (dial < 1f || dial > 5f) " ⚠ That's outside a 1–5 A dial, so the wrong CT ratio or relay for this motor." else "")
    Explain("FLC ≈ kW × 1000 ÷ (1.732 × V × pf × eff). Rule of thumb at 1000 V: about 0.7 A per kW. $tableTxt $ctTxt")
}

// ------------------------------------------------------------------ earth leakage relay + test circuit + grading

@Composable
internal fun ElrTestSim() {
    val settings = listOf(30, 100, 200, 300, 500, 1000)
    var si by remember { mutableIntStateOf(1) }
    var leak by remember { mutableFloatStateOf(20f) }
    var res by remember { mutableIntStateOf(0) }
    val setMa = settings[si]
    val tripped = leak >= setMa
    val phase = rememberFlow(0.6f)
    val ink = ink()
    PillRow(settings.map { "$it mA" }, si) { si = it }
    LabeledSlider("Earth leakage on the cable: ${leak.toInt()} mA", leak, 0f..1200f) { leak = it }
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val cx = size.width * 0.32f
        val cy = size.height * 0.5f
        // toroid
        drawOval(Color(0xFF616161), Offset(cx - 38.dp.toPx(), cy - 50.dp.toPx()), Size(76.dp.toPx(), 100.dp.toPx()), style = Stroke(12.dp.toPx()))
        val cols = listOf(Palette.Active, Color(0xFFFFFFFF), LBlue)
        for (i in 0..2) {
            val yy = cy - 18.dp.toPx() + i * 18.dp.toPx()
            drawLine(cols[i], Offset(0f, yy), Offset(size.width * 0.62f, yy), 5.dp.toPx())
            drawLine(ink, Offset(0f, yy), Offset(size.width * 0.62f, yy), 1.dp.toPx())
        }
        // leakage path out the end to earth
        if (leak > 1f) {
            val p0 = Offset(size.width * 0.62f, cy)
            val p1 = Offset(size.width * 0.62f, size.height - 14.dp.toPx())
            drawLine(LAmber, p0, p1, 3.dp.toPx())
            earthSymbol(p1, ink)
            val f = (phase % 1f)
            drawCircle(LAmber, 4.dp.toPx(), Offset(p0.x, p0.y + (p1.y - p0.y) * f))
        }
        label("toroid", cx, cy - 58.dp.toPx(), ink, 10f)
        // relay box
        val rx = size.width * 0.72f
        box(Offset(rx, 14.dp.toPx()), Size(size.width * 0.25f, 70.dp.toPx()), Color(0xFFF5F5F5), ink)
        label("E/L RELAY", rx + size.width * 0.125f, 32.dp.toPx(), Color.Black, 10f)
        label("set $setMa mA", rx + size.width * 0.125f, 50.dp.toPx(), Color.Black, 10f)
        drawCircle(if (tripped) LAmber else Color(0xFFBDBDBD), 8.dp.toPx(), Offset(rx + size.width * 0.125f, 70.dp.toPx()))
        label(if (tripped) "TRIPPED" else "healthy", rx + size.width * 0.125f, 104.dp.toPx(), if (tripped) Palette.Bad else Palette.Good, 12f)
    }
    Explain(
        "In and out currents in the 3 phases normally add up to zero inside the toroid. Any current leaking to earth doesn't come back through it, so the toroid sees the difference. " +
            if (tripped) "${leak.toInt()} mA ≥ $setMa mA, so it TRIPS." else "${leak.toInt()} mA < $setMa mA, so it holds in."
    )
    Text("The E/L TEST button: 24 V through a resistor, with one wire through the toroid", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
    val rs = listOf(220, 680, 1000)
    PillRow(rs.map { "$it Ω" }, res) { res = it }
    val testMa = 24000f / rs[res]
    Explain(
        "I = V ÷ R = 24 ÷ ${rs[res]} = ${f1(testMa)} mA. Setting $setMa mA → " +
            (if (testMa >= setMa) "trips ✅ (the test proves it)." else "WON'T trip ❌. The test resistor must make MORE current than the setting. Wrong resistor or wrong setting.") +
            " (A real relay trips somewhere at or a bit below its setting. The test current must sit above it.)"
    )
    Text("Grading: smaller settings close to the machine", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
    val grade = listOf("Sub NER" to 1000, "Sub main CB" to 1000, "Sub CBs" to 500, "1st DB" to 300, "2nd DB" to 200, "Jumbo box" to 100, "Pump starter" to 30)
    Canvas(Modifier.fillMaxWidth().height(110.dp)) {
        val n = grade.size
        val bw = size.width / n
        grade.forEachIndexed { i, (name, ma) ->
            val h = (log10(ma.toFloat()) / 3f) * (size.height - 36.dp.toPx())
            val x = i * bw + bw * 0.15f
            val lit = leak >= ma
            drawRect(if (lit) LAmber else Palette.Good.copy(alpha = 0.6f), Offset(x, size.height - 24.dp.toPx() - h), Size(bw * 0.7f, h))
            label("$ma", x + bw * 0.35f, size.height - 28.dp.toPx() - h, ink, 9f)
            label(name, x + bw * 0.35f, size.height - 8.dp.toPx(), ink, 8f)
        }
    }
    val tripsAt = grade.filter { leak >= it.second }.map { it.first }
    Explain(
        if (tripsAt.isEmpty()) "Nothing trips at ${leak.toInt()} mA."
        else "At ${leak.toInt()} mA these would ALL want to trip: ${tripsAt.joinToString()}. The one closest to the fault should clear first. That's why upstream relays get small time delays (the NER has 300–400 ms). Then only the faulty branch goes off, not the whole mine."
    )
}

// ------------------------------------------------------------------ pilot (earth continuity) relay

@Composable
internal fun PilotRelaySim() {
    val trips = listOf(10, 15, 20, 25, 35, 45)
    var ti by remember { mutableIntStateOf(3) }
    var loop by remember { mutableFloatStateOf(8f) }
    var open by remember { mutableStateOf(false) }
    var short by remember { mutableStateOf(false) }
    val trip = trips[ti]
    val tooHigh = loop > trip
    val healthy = !open && !short && !tooHigh
    val ink = ink()
    val phase = rememberFlow(if (healthy) 0.4f else 0f)
    PillRow(trips.map { "Trip $it Ω" }, ti) { ti = it }
    LabeledSlider("Pilot + earth loop resistance: ${f1(loop)} Ω (long/old cable, dirty pins = higher)", loop, 0f..60f) { loop = it }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedButton({ open = !open }, Modifier.weight(1f)) { Text(if (open) "Repair pilot" else "Break pilot core", fontSize = 11.sp) }
        OutlinedButton({ short = !short }, Modifier.weight(1f)) { Text(if (short) "Remove short" else "Short pilot to earth", fontSize = 11.sp) }
    }
    Canvas(Modifier.fillMaxWidth().height(140.dp)) {
        val l = 70.dp.toPx(); val r = size.width - 30.dp.toPx()
        val yP = 40.dp.toPx(); val yE = 110.dp.toPx()
        // relay
        box(Offset(4.dp.toPx(), 22.dp.toPx()), Size(60.dp.toPx(), 100.dp.toPx()), Color(0xFFFF9800), ink)
        label("PILOT", 34.dp.toPx(), 40.dp.toPx(), Color.Black, 9f)
        label("RELAY", 34.dp.toPx(), 52.dp.toPx(), Color.Black, 9f)
        val leds = listOf(Triple("Healthy", Palette.Good, healthy), Triple("Short", Palette.Bad, short), Triple("Open", Palette.Bad, !short && (open || tooHigh)))
        leds.forEachIndexed { i, (t, c, on) ->
            val y = 70.dp.toPx() + i * 16.dp.toPx()
            drawCircle(if (on) c else Color(0xFF6D4C41), 4.dp.toPx(), Offset(14.dp.toPx(), y))
            label(t, 40.dp.toPx(), y + 4.dp.toPx(), Color.Black, 8f)
        }
        // pilot core
        val mid = (l + r) / 2
        if (open) {
            drawLine(Palette.Current, Offset(l, yP), Offset(mid - 10.dp.toPx(), yP), 3.dp.toPx())
            drawLine(Palette.Current, Offset(mid + 10.dp.toPx(), yP), Offset(r, yP), 3.dp.toPx())
            label("✂", mid, yP + 5.dp.toPx(), Palette.Bad, 14f)
        } else drawLine(Palette.Current, Offset(l, yP), Offset(r, yP), 3.dp.toPx())
        label("pilot core", mid, yP - 8.dp.toPx(), ink, 10f)
        earthLine(Offset(l, yE), Offset(r, yE), 4.dp.toPx())
        label("earth core", mid, yE + 16.dp.toPx(), ink, 10f)
        // diode at machine end
        val dTop = Offset(r, yP); val dBot = Offset(r, yE)
        drawLine(ink, dTop, dBot, 2.dp.toPx())
        val tri = Path().apply {
            moveTo(r - 9.dp.toPx(), 66.dp.toPx()); lineTo(r + 9.dp.toPx(), 66.dp.toPx()); lineTo(r, 82.dp.toPx()); close()
        }
        drawPath(tri, ink)
        drawLine(ink, Offset(r - 9.dp.toPx(), 82.dp.toPx()), Offset(r + 9.dp.toPx(), 82.dp.toPx()), 2.dp.toPx())
        label("diode", r - 22.dp.toPx(), 78.dp.toPx(), ink, 9f)
        if (short) {
            drawLine(Palette.Bad, Offset(mid + 40.dp.toPx(), yP), Offset(mid + 40.dp.toPx(), yE), 3.dp.toPx())
            label("short", mid + 58.dp.toPx(), (yP + yE) / 2, Palette.Bad, 10f)
        }
        if (healthy) {
            val pts = listOf(Offset(l, yP), Offset(r, yP), Offset(r, yE), Offset(l, yE))
            dotsAlong(pts, phase, 10, Palette.Current, 3.dp.toPx())
        }
    }
    Explain(
        when {
            short -> "SHORT CIRCUIT trip. The relay sees the pilot going straight to earth, bypassing the diode. It checks the diode is really there (it sends ±24 V and expects current one way only). That stops a pilot touching earth anywhere in the cable from 'faking' a healthy earth."
            open -> "OPEN CIRCUIT trip. Broken pilot core, plug not fully in, or the diode is open. No loop = no proof the machine is earthed = no power."
            tooHigh -> "TRIP: the loop is ${f1(loop)} Ω, above the $trip Ω setting. The earth path is too poor to trust (long cable, corroded pins, damaged earth core)."
            else -> "HEALTHY. The loop (pilot → diode → earth back) is ${f1(loop)} Ω, under $trip Ω, so the outlet may close. If the earth core breaks, the loop breaks too, and the power goes off."
        }
    )
}

// ------------------------------------------------------------------ pump snore controller

@Composable
internal fun SnoreSim() {
    var level by remember { mutableFloatStateOf(0.7f) }
    var running by remember { mutableStateOf(true) }
    var setpoint by remember { mutableFloatStateOf(0f) }
    var below by remember { mutableFloatStateOf(0f) }
    var starts by remember { mutableIntStateOf(0) }
    var window by remember { mutableIntStateOf(1) }
    val windows = listOf(5, 10, 15)
    var msg by remember { mutableStateOf("Setpoint not set yet: the pump will run dry ('snore') and never stop. Wait until it's snoring, then press SET.") }
    val snoring = running && level < 0.15f
    val current = if (!running) 0f else if (snoring) 50f else 80f
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { t ->
                if (last != 0L) {
                    val dt = (t - last) / 1e9f
                    val inflow = 0.05f
                    level = (level + dt * (inflow - if (running) 0.18f else 0f)).coerceIn(0f, 1f)
                    val cur = if (!running) 0f else if (level < 0.15f) 50f else 80f
                    if (running && setpoint > 0f && cur < setpoint) {
                        below += dt
                        if (below > 1f) { running = false; below = 0f }
                    } else below = 0f
                    if (!running && level > 0.75f) { running = true; starts++ }
                }
                last = t
            }
        }
    }
    val ink = ink()
    PillRow(windows.map { "Window $it%" }, window) { window = it }
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        // sump
        val sx = 10.dp.toPx(); val sw = size.width * 0.35f; val st = 20.dp.toPx(); val sh = size.height - 30.dp.toPx()
        drawRect(Palette.Water.copy(alpha = 0.7f), Offset(sx, st + sh * (1 - level)), Size(sw, sh * level))
        drawRect(ink, Offset(sx, st), Size(sw, sh), style = Stroke(2.dp.toPx()))
        val px = sx + sw / 2
        drawRect(Color(0xFF455A64), Offset(px - 12.dp.toPx(), st + sh - 30.dp.toPx()), Size(24.dp.toPx(), 28.dp.toPx()))
        label(if (snoring) "slurp… (air)" else if (running) "pumping" else "paused", px, st - 6.dp.toPx(), if (snoring) Palette.Bad else ink, 11f)
        // current meter
        val gx = size.width * 0.5f; val gw = size.width * 0.45f
        val gy = st; val gh = sh
        drawRect(ink, Offset(gx, gy), Size(gw, gh), style = Stroke(1.5.dp.toPx()))
        fun yOf(pct: Float) = gy + gh * (1 - pct / 100f)
        drawRect(if (snoring) LAmber else Palette.Good, Offset(gx + gw * 0.15f, yOf(current)), Size(gw * 0.3f, gh - (yOf(current) - gy)))
        label("${current.toInt()}%", gx + gw * 0.3f, yOf(current) - 4.dp.toPx(), ink, 11f)
        if (setpoint > 0f) {
            drawLine(Palette.Bad, Offset(gx, yOf(setpoint)), Offset(gx + gw, yOf(setpoint)), 2.dp.toPx())
            label("stop below ${setpoint.toInt()}%", gx + gw * 0.72f, yOf(setpoint) - 4.dp.toPx(), Palette.Bad, 9f)
        }
        label("normal 80%", gx + gw * 0.72f, yOf(80f) + 12.dp.toPx(), ink, 9f)
        label("snoring 50%", gx + gw * 0.72f, yOf(50f) + 12.dp.toPx(), ink, 9f)
    }
    Text("Starts so far: $starts", style = MaterialTheme.typography.labelLarge)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedButton({
            if (!running) msg = "The pump must be RUNNING when you press SET."
            else { setpoint = current + windows[window]
            msg = if (snoring) "Set while snoring: ${current.toInt()}% + ${windows[window]}% = ${setpoint.toInt()}%. Normal running (80%) is above that, so it keeps pumping. Snoring (50%) is below, so it pauses. ✅"
            else "Set while pumping NORMALLY: ${current.toInt()}% + ${windows[window]}% = ${setpoint.toInt()}%. Now even normal running is 'too low', so the pump stops almost straight away and short-cycles. ❌ Set it while it's snoring." }
        }, Modifier.weight(1f)) { Text("Press SET (▲+▼)", fontSize = 11.sp) }
        OutlinedButton({ setpoint = 0f; starts = 0; msg = "Setpoint cleared." }, Modifier.weight(1f)) { Text("Clear setpoint", fontSize = 11.sp) }
    }
    Explain(msg)
}

// ------------------------------------------------------------------ fan start inrush staging

@Composable
internal fun FanStagingSim() {
    var staged by remember { mutableStateOf(true) }
    var gap by remember { mutableFloatStateOf(10f) }
    val flc = 79f
    val lra = 7f
    val ink = ink()
    PillRow(listOf("Both fans start together", "Staged (timer)"), if (staged) 1 else 0) { staged = it == 1 }
    if (staged) LabeledSlider("Gap between starts: ${gap.toInt()} s", gap, 2f..40f) { gap = it }
    fun one(t: Float): Float = if (t < 0f) 0f else flc + (lra - 1f) * flc * exp(-t / 2.5f)
    val g = if (staged) gap else 0f
    val tmax = max(20f, g + 15f)
    var peak = 0f
    for (i in 0..400) { val t = tmax * i / 400f; peak = max(peak, one(t) + one(t - g)) }
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val l = 36.dp.toPx(); val b = size.height - 18.dp.toPx(); val t0 = 10.dp.toPx()
        val ymax = 1200f
        fun X(t: Float) = l + (size.width - l - 6.dp.toPx()) * t / tmax
        fun Y(a: Float) = b - (b - t0) * min(a, ymax) / ymax
        drawLine(ink, Offset(l, t0), Offset(l, b), 1.5.dp.toPx())
        drawLine(ink, Offset(l, b), Offset(size.width, b), 1.5.dp.toPx())
        for (a in listOf(250f, 500f, 1000f)) {
            drawLine(ink.copy(alpha = 0.2f), Offset(l, Y(a)), Offset(size.width, Y(a)), 1.dp.toPx())
            label("${a.toInt()}", l - 4.dp.toPx(), Y(a) + 4.dp.toPx(), ink, 8f, android.graphics.Paint.Align.RIGHT)
        }
        var prev = Offset(X(0f), Y(one(0f) + one(-g)))
        for (i in 1..300) {
            val t = tmax * i / 300f
            val p = Offset(X(t), Y(one(t) + one(t - g)))
            drawLine(Palette.Bad, prev, p, 2.5.dp.toPx())
            prev = p
        }
        label("total current from the supply (A)", size.width / 2, t0 + 4.dp.toPx(), ink, 10f)
        label("time →", size.width - 24.dp.toPx(), size.height - 2.dp.toPx(), ink, 9f)
    }
    Explain(
        "Each 110 kW fan runs at about $flc A but, started direct-on-line, can draw roughly 6–8× that for a few seconds (we've used ${lra.toInt()}×). " +
            "Peak here ≈ ${peak.toInt()} A. " +
            if (staged) "Staggering the starts keeps the peak near ONE motor's starting current. That means less voltage dip for everything else on the cable, and less chance of tripping the main breaker or upstream protection."
            else "Starting both together roughly DOUBLES the peak. That's a big voltage dip on a long 1000 V cable, and it might trip upstream protection."
    )
}

// ------------------------------------------------------------------ leaky feeder signal (dB)

@Composable
internal fun LeakyFeederSim() {
    var dist by remember { mutableFloatStateOf(300f) }
    var amp by remember { mutableStateOf(true) }
    var split by remember { mutableStateOf(false) }
    val lossPer100 = 3f      // illustrative round numbers only
    val ampGain = 20f
    val ampAt = 350f
    val ink = ink()
    LabeledSlider("Distance from the head end: ${dist.toInt()} m", dist, 0f..800f) { dist = it }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedButton({ amp = !amp }, Modifier.weight(1f)) { Text(if (amp) "Remove amplifier" else "Fit amplifier at 350 m", fontSize = 11.sp) }
        OutlinedButton({ split = !split }, Modifier.weight(1f)) { Text(if (split) "Remove splitter" else "2-way splitter at 100 m", fontSize = 11.sp) }
    }
    fun sig(d: Float): Float {
        var s = 0f - lossPer100 * d / 100f
        if (split && d > 100f) s -= 3f
        if (amp && d > ampAt) s += ampGain
        return s
    }
    val s = sig(dist)
    val ok = s > -20f
    Canvas(Modifier.fillMaxWidth().height(130.dp)) {
        val l = 10.dp.toPx(); val r = size.width - 10.dp.toPx()
        val y = 30.dp.toPx()
        drawLine(Color(0xFF795548), Offset(l, y), Offset(r, y), 5.dp.toPx())
        fun X(d: Float) = l + (r - l) * d / 800f
        box(Offset(l - 4.dp.toPx(), y - 12.dp.toPx()), Size(24.dp.toPx(), 24.dp.toPx()), Color(0xFF90CAF9), ink)
        label("head", l + 8.dp.toPx(), y + 26.dp.toPx(), ink, 8f)
        if (split) { drawCircle(LAmber, 7.dp.toPx(), Offset(X(100f), y)); label("split −3 dB", X(100f), y + 26.dp.toPx(), ink, 8f) }
        if (amp) { box(Offset(X(ampAt) - 10.dp.toPx(), y - 10.dp.toPx()), Size(20.dp.toPx(), 20.dp.toPx()), Palette.Good, ink); label("amp +20", X(ampAt), y + 26.dp.toPx(), ink, 8f) }
        drawCircle(Palette.Bad, 6.dp.toPx(), Offset(X(dist), y))
        // signal graph
        val gb = size.height - 6.dp.toPx(); val gt = y + 40.dp.toPx()
        fun Y(db: Float) = gt + (gb - gt) * (-db / 30f).coerceIn(0f, 1f)
        drawLine(Palette.Bad.copy(alpha = 0.5f), Offset(l, Y(-20f)), Offset(r, Y(-20f)), 1.dp.toPx())
        label("radios drop out below here", r - 70.dp.toPx(), Y(-20f) - 3.dp.toPx(), Palette.Bad, 8f)
        var prev = Offset(X(0f), Y(sig(0f)))
        for (i in 1..200) {
            val d = 800f * i / 200f
            val p = Offset(X(d), Y(sig(d)))
            if (abs(p.y - prev.y) < 40.dp.toPx()) drawLine(LBlue, prev, p, 2.dp.toPx())
            prev = p
        }
    }
    val ratio = 10f.pow(s / 10f)
    Explain(
        "Signal here: ${f1(s)} dB, which is ${if (ratio >= 0.01f) f2(ratio) else "<0.01"} × the head-end power. " +
            "Every −3 dB is HALF the power, and −10 dB is a tenth. The cable loses signal as it 'leaks' it out to radios along the drive, so amplifiers are spaced along the line to top it back up. " +
            (if (ok) "Radios work here." else "Too weak: radios drop out. Check for a dead amplifier (often powered down the coax itself), a damaged cable, or a bad joint.") +
            " (Loss numbers in this demo are round, made-up values to show the idea.)"
    )
}
