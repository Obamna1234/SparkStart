package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.ui.Palette
import kotlinx.coroutines.delay
import kotlin.random.Random

private val Amber = Color(0xFFFF9800)

// ------------------------------------------------------------------ pilot chain

private val chainParts = listOf(
    "Pilot core + earth in the trailing cable",
    "Slip-ring cover limit switch (cover on)",
    "Electrical cabinet door switches (doors shut)",
    "Main isolator ON (pilot runs through it)",
    "Breaker undervoltage relays reset",
    "Diesel ignition OFF (anti-tram interlock)",
    "Pilot diode at the machine end"
)

@Composable
internal fun PilotChainSim() {
    var states by remember { mutableStateOf(List(chainParts.size) { true }) }
    val firstBreak = states.indexOfFirst { !it }
    val healthy = firstBreak < 0
    val ink = ink()
    val phase = rememberFlow(if (healthy) 0.25f else 0f)
    Canvas(Modifier.fillMaxWidth().height(110.dp)) {
        val n = chainParts.size
        val l = 26.dp.toPx()
        val r = size.width - 26.dp.toPx()
        val y = size.height * 0.45f
        val step = (r - l) / n
        box(Offset(2.dp.toPx(), y - 18.dp.toPx()), Size(22.dp.toPx(), 36.dp.toPx()), Color(0xFFECEFF1), ink)
        label("relay", 13.dp.toPx(), y + 32.dp.toPx(), ink, 9f)
        for (i in 0 until n) {
            val x0 = l + i * step
            val x1 = x0 + step
            val lit = healthy || i < firstBreak
            val col = if (lit) Palette.Good else Color(0xFF9E9E9E)
            if (states[i]) {
                drawLine(col, Offset(x0, y), Offset(x1, y), 4.dp.toPx())
            } else {
                drawLine(col, Offset(x0, y), Offset(x0 + step * 0.35f, y), 4.dp.toPx())
                drawLine(Palette.Bad, Offset(x0 + step * 0.35f, y), Offset(x0 + step * 0.65f, y - 14.dp.toPx()), 4.dp.toPx())
                drawLine(Color(0xFF9E9E9E), Offset(x0 + step * 0.65f, y), Offset(x1, y), 4.dp.toPx())
            }
            drawCircle(if (states[i]) col else Palette.Bad, 6.dp.toPx(), Offset(x0 + step / 2, y + if (states[i]) 0f else 16.dp.toPx()))
            label("${i + 1}", x0 + step / 2, y + 30.dp.toPx(), ink, 10f)
        }
        if (healthy) {
            for (k in 0 until 10) {
                val f = (phase + k / 10f) % 1f
                drawCircle(Palette.Current, 3.5.dp.toPx(), Offset(l + f * (r - l), y))
            }
        }
        label(if (healthy) "PILOT HEALTHY: outlet can close" else "PILOT BROKEN at ${firstBreak + 1}: no power", size.width / 2, 16.dp.toPx(), if (healthy) Palette.Good else Palette.Bad, 12f)
    }
    Text("Tap a part to open or close it:", style = MaterialTheme.typography.labelLarge)
    chainParts.forEachIndexed { i, p ->
        Text(
            "${if (states[i]) "✅" else "❌"}  ${i + 1}. $p",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (states[i]) MaterialTheme.colorScheme.surface else Palette.Bad.copy(alpha = 0.2f))
                .clickable { states = states.toMutableList().also { it[i] = !it[i] } }
                .padding(8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    Explain(
        if (healthy) "Every part of the pilot circuit is in SERIES. Only when ALL of them are healthy does the supply outlet let the contactor close and power the rig."
        else "One open part anywhere breaks the whole loop, so the outlet won't close (or trips). To fault-find, work along the chain in order. Check each switch, then the plug pins and the cable. Never bridge out a part to 'get it going'."
    )
}

// ------------------------------------------------------------------ outlet start sequence

@Composable
internal fun OutletStartSim() {
    var insulFault by remember { mutableStateOf(false) }
    var pilotOpen by remember { mutableStateOf(false) }
    var frozen by remember { mutableStateOf(false) }
    var run by remember { mutableIntStateOf(0) }
    var stopRun by remember { mutableIntStateOf(0) }
    var step by remember { mutableIntStateOf(-1) }
    var running by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("Set up any faults, then press START.") }
    var bad by remember { mutableStateOf(false) }

    LaunchedEffect(run) {
        if (run == 0) return@LaunchedEffect
        running = false
        bad = false
        step = 0; result = "1. Checking for any active trips…"; delay(900)
        step = 1; result = "2. Earth continuity: is the pilot/earth loop healthy?"; delay(1100)
        if (pilotOpen) { bad = true; result = "TRIP: Earth Continuity (EC). The pilot loop is open, so the outlet will not close. Check the plug, pilot core and machine-end diode."; return@LaunchedEffect }
        step = 2; result = "3. Earth Fault Lockout: a small test voltage checks the outgoing cable insulation BEFORE closing…"; delay(1300)
        if (insulFault) { bad = true; result = "TRIP: Earth Fault Lockout (EFLO). Insulation fault found on the cable/machine. The outlet never closed onto the fault. Isolate and insulation test to find it."; return@LaunchedEffect }
        step = 3; result = "4. Closing the main contactor…"; delay(900)
        step = 4; running = true; result = "RUNNING. Earth leakage, earth continuity, overload and other protections are now watching continuously."
    }
    LaunchedEffect(stopRun) {
        if (stopRun == 0) return@LaunchedEffect
        result = "STOP pressed: contactor told to open…"
        delay(1200)
        if (frozen) { bad = true; running = false; step = 5; result = "TRIP: Frozen Contact (FC). Voltage was still on the outgoing side after the contactor should have opened (welded contacts). The protection trips the outlet's circuit breaker to kill it." }
        else { running = false; step = -1; result = "Stopped normally. Contactor opened and the outgoing side is dead." }
    }

    Text("Faults to hide in the system:", style = MaterialTheme.typography.labelLarge)
    Row(Modifier.fillMaxWidth()) {
        PillToggle("Cable insulation fault", insulFault) { insulFault = it }
    }
    Row(Modifier.fillMaxWidth()) {
        PillToggle("Pilot loop open", pilotOpen) { pilotOpen = it }
        PillToggle("Welded contactor", frozen) { frozen = it }
    }
    val steps = listOf("Trips?", "Earth cont.", "EFLO test", "Close", "Running")
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        steps.forEachIndexed { i, s ->
            val done = step > i || (step == i && !bad)
            val failed = bad && step == i
            Box(
                Modifier.weight(1f).padding(2.dp).clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            failed -> Palette.Bad
                            done -> Palette.Good
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ).padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) { Text(s, fontSize = 11.sp, color = if (done || failed) Color.White else MaterialTheme.colorScheme.onSurface) }
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { run++ }, enabled = !running) { Text("START outlet") }
        Spacer(Modifier.width(8.dp))
        OutlinedButton(onClick = { stopRun++ }, enabled = running) { Text("STOP") }
    }
    Explain(result, if (bad) Palette.Bad else Color.Unspecified)
    Explain("A smart mining outlet won't just slam power onto a cable. It checks the earth loop and the cable insulation first. Frozen-contact protection covers the opposite problem: the contactor failing to open.")
}

@Composable
private fun PillToggle(text: String, on: Boolean, onChange: (Boolean) -> Unit) {
    Text(
        (if (on) "⚠️ " else "○ ") + text,
        fontSize = 12.sp,
        color = if (on) Color.White else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(50))
            .background(if (on) Palette.Bad else MaterialTheme.colorScheme.surface)
            .clickable { onChange(!on) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

// ------------------------------------------------------------------ diode test

@Composable
internal fun DiodeTestSim() {
    var cond by remember { mutableIntStateOf(0) }
    var swapped by remember { mutableStateOf(false) }
    val reading = when (cond) {
        0 -> if (!swapped) "0.55 V" else "OL"
        1 -> "OL"
        else -> "0.00 V"
    }
    val ok = cond == 0
    val ink = ink()
    PillRow(listOf("Healthy diode", "Open (diode/pilot broken)", "Short (pilot to earth)"), cond) { cond = it }
    Canvas(Modifier.fillMaxWidth().height(110.dp)) {
        val y1 = size.height * 0.35f
        val y2 = size.height * 0.7f
        val l = size.width * 0.18f
        val r = size.width * 0.85f
        drawLine(Amber, Offset(l, y1), Offset(r, y1), 4.dp.toPx())
        earthLine(Offset(l, y2), Offset(r, y2), 4.dp.toPx())
        label("pilot pin", l - 4.dp.toPx(), y1 + 4.dp.toPx(), ink, 10f, Paint.Align.RIGHT)
        label("earth pin", l - 4.dp.toPx(), y2 + 4.dp.toPx(), ink, 10f, Paint.Align.RIGHT)
        // diode at machine end
        val cx = r
        val cy = (y1 + y2) / 2
        drawLine(ink, Offset(cx, y1), Offset(cx, cy - 10.dp.toPx()), 3.dp.toPx())
        drawLine(ink, Offset(cx, cy + 10.dp.toPx()), Offset(cx, y2), 3.dp.toPx())
        when (cond) {
            0 -> {
                val p = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx - 10.dp.toPx(), cy - 10.dp.toPx())
                    lineTo(cx + 10.dp.toPx(), cy - 10.dp.toPx())
                    lineTo(cx, cy + 8.dp.toPx())
                    close()
                }
                drawPath(p, ink)
                drawLine(ink, Offset(cx - 10.dp.toPx(), cy + 10.dp.toPx()), Offset(cx + 10.dp.toPx(), cy + 10.dp.toPx()), 3.dp.toPx())
            }
            1 -> label("✕ open", cx + 8.dp.toPx(), cy + 4.dp.toPx(), Palette.Bad, 12f, Paint.Align.LEFT)
            else -> {
                drawLine(Palette.Bad, Offset(cx, cy - 10.dp.toPx()), Offset(cx, cy + 10.dp.toPx()), 3.dp.toPx())
                label("short", cx + 8.dp.toPx(), cy + 4.dp.toPx(), Palette.Bad, 12f, Paint.Align.LEFT)
            }
        }
        label("machine end", cx, size.height - 2.dp.toPx(), ink, 9f)
    }
    Box(
        Modifier.fillMaxWidth().padding(vertical = 6.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF263238)).padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(reading, color = Color(0xFFB2FF59), fontSize = 34.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Text(if (swapped) "red lead on earth, black on pilot" else "red lead on pilot, black on earth", color = Color(0xFFB0BEC5), fontSize = 11.sp)
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { swapped = !swapped }) { Text("Swap the meter leads") }
    }
    Explain(
        when (cond) {
            0 -> if (!swapped) "About 0.5–0.6 V one way… now swap the leads." else "…and open circuit (OL) the other way. A diode only passes current in ONE direction. That's a healthy pilot loop. ✅"
            1 -> "OL both ways = nothing connected. The diode is open or the pilot/earth is broken somewhere (plug, cable, slip ring). ❌"
            else -> "Near 0 V both ways (a reading in BOTH directions) = shorted diode or pilot touching earth. ❌ The monitoring relay must trip on a short too, because a shorted pilot could fool it."
        },
        if (ok) Color.Unspecified else Palette.Bad
    )
    Explain("Done with the multimeter on DIODE TEST, plug isolated and locked out, across the pilot and earth pins of the plug. Change the diode or look for a break if the voltage is lower than expected, you get a reading both ways, or there's no reading at all.")
}

// ------------------------------------------------------------------ XLPE cable layers

private data class Layer(val name: String, val color: Color, val info: String)

private val layers = listOf(
    Layer("Copper conductor", Color(0xFFB87333), "Carries the current. In 11 kV mine cables it's stranded copper."),
    Layer("Conductor screen (semicon)", Color(0xFF212121), "A thin semi-conductive layer bonded to the conductor. It smooths the electric field so there are no sharp points or air gaps at the conductor surface."),
    Layer("XLPE insulation", Color(0xFFE0F2F1), "Cross-linked polyethylene: the main insulation that holds back thousands of volts. Any nick, dirt or moisture here can become a failure point, which is why it's cleaned carefully during jointing."),
    Layer("Insulation screen (semicon)", Color(0xFF424242), "Another semi-conductive layer on the outside of the XLPE. It keeps the electric field evenly inside the insulation. Where it's cut back at a termination, the field crowds at the edge, and that's why stress-control mastic and tubes are fitted."),
    Layer("Copper wire screen", Color(0xFFFFB74D), "Earthed copper wires around each core (or the cable). They carry charging and fault current to earth and make the outside of the cable safe. You fold these back and earth them at terminations."),
    Layer("Outer sheath", Color(0xFFC62828), "Tough outer layer keeping out water and mechanical damage. It's checked with a sheath test, an insulation test between the metallic screen and the outside of the sheath.")
)

@Composable
internal fun XlpeLayersSim() {
    var sel by remember { mutableIntStateOf(3) }
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(200.dp)) {
        val c = Offset(size.width * 0.32f, size.height / 2)
        val maxR = size.height * 0.46f
        val radii = listOf(0.28f, 0.34f, 0.66f, 0.72f, 0.82f, 1.0f)
        for (i in layers.indices.reversed()) {
            val rr = maxR * radii[i]
            drawCircle(layers[i].color, rr, c)
            if (i == sel) drawCircle(Color(0xFF2979FF), rr, c, style = Stroke(4.dp.toPx()))
        }
        // copper screen wires dots
        val wr = maxR * 0.77f
        for (k in 0 until 24) {
            val a = k * 2 * Math.PI / 24
            drawCircle(Color(0xFFE65100), 2.5.dp.toPx(), Offset(c.x + (wr * kotlin.math.cos(a)).toFloat(), c.y + (wr * kotlin.math.sin(a)).toFloat()))
        }
        label(layers[sel].name, size.width * 0.66f, size.height * 0.45f, ink, 12f, Paint.Align.LEFT)
        label("(tap a layer below)", size.width * 0.66f, size.height * 0.45f + 16.dp.toPx(), ink, 9f, Paint.Align.LEFT)
    }
    PillRow(layers.map { it.name }, sel) { sel = it }
    Explain(layers[sel].info)
}

// ------------------------------------------------------------------ two-stage fan rotation

@Composable
internal fun FanRotationSim() {
    var round by remember { mutableIntStateOf(0) }
    var s1ok by remember(round) { mutableStateOf(Random.nextBoolean()) }
    var s2ok by remember(round) { mutableStateOf(Random.nextBoolean()) }
    var running by remember(round) { mutableIntStateOf(0) } // 0 none, 1 stage1, 2 stage2, 3 both
    var msg by remember(round) { mutableStateOf("Rotation is unknown after installing a new fan. Jog each stage ON ITS OWN and watch the vent bag.") }
    val flow = when (running) {
        1 -> if (s1ok) 1f else -1f
        2 -> if (s2ok) 1f else -1f
        3 -> (if (s1ok) 1f else -1f) + (if (s2ok) 1f else -1f)
        else -> 0f
    }
    val bag by animateFloatAsState(((flow + 2f) / 4f).coerceIn(0f, 1f), tween(900), label = "bag")
    val spin1 = rememberFlow(if (running == 1 || running == 3) (if (s1ok) 1.2f else -1.2f) else 0f)
    val spin2 = rememberFlow(if (running == 2 || running == 3) (if (s2ok) 1.2f else -1.2f) else 0f)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val cy = size.height / 2
        val fw = size.width * 0.14f
        val fh = size.height * 0.6f
        val x1 = size.width * 0.06f
        val x2 = x1 + fw + 4.dp.toPx()
        listOf(x1 to spin1, x2 to spin2).forEachIndexed { i, (x, sp) ->
            drawRect(Color(0xFF78909C), Offset(x, cy - fh / 2), Size(fw, fh))
            val c = Offset(x + fw / 2, cy)
            val a = sp * 360f
            rotate(a, c) {
                drawLine(ink, Offset(c.x, c.y - fh * 0.35f), Offset(c.x, c.y + fh * 0.35f), 4.dp.toPx())
                drawLine(ink, Offset(c.x - fw * 0.35f, c.y), Offset(c.x + fw * 0.35f, c.y), 4.dp.toPx())
            }
            label("stage ${i + 1}", x + fw / 2, cy + fh / 2 + 14.dp.toPx(), ink, 10f)
        }
        // vent bag
        val bx = x2 + fw
        val bw = size.width - bx - 6.dp.toPx()
        val bh = fh * (0.25f + 0.85f * bag)
        drawRoundRect(Color(0xFFFFEB3B).copy(alpha = 0.85f), Offset(bx, cy - bh / 2), Size(bw, bh), androidx.compose.ui.geometry.CornerRadius(bh / 2, bh / 2))
        label(
            when {
                flow > 0.5f -> "vent bag INFLATES ✅"
                flow < -0.5f -> "bag DEFLATES / sucks in ❌"
                running != 0 -> "weak / fighting airflow ⚠️"
                else -> "vent bag (fan stopped)"
            },
            bx + bw / 2, cy + 4.dp.toPx(), Color.Black, 11f
        )
        label("airflow →", bx + bw / 2, cy - fh / 2 - 6.dp.toPx(), ink, 10f)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = { running = 1; msg = if (s1ok) "Stage 1 jogged: bag inflates. Stage 1 is correct." else "Stage 1 jogged: bag deflates. Stage 1 is BACKWARDS." }) { Text("Jog stage 1", fontSize = 12.sp) }
        Spacer(Modifier.width(4.dp))
        OutlinedButton(onClick = { running = 2; msg = if (s2ok) "Stage 2 jogged: bag inflates. Stage 2 is correct." else "Stage 2 jogged: bag deflates. Stage 2 is BACKWARDS." }) { Text("Jog stage 2", fontSize = 12.sp) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = { running = 0; s1ok = !s1ok; msg = "Isolated, locked, proved dead, then swapped two phases on stage 1. Re-test it." }) { Text("Swap 2 phases: S1", fontSize = 12.sp) }
        Spacer(Modifier.width(4.dp))
        OutlinedButton(onClick = { running = 0; s2ok = !s2ok; msg = "Isolated, locked, proved dead, then swapped two phases on stage 2. Re-test it." }) { Text("Swap 2 phases: S2", fontSize = 12.sp) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = {
            running = 3
            msg = if (s1ok && s2ok) "Both stages correct and running together. Full airflow. ✅ Test complete." else "Running both with one backwards. They fight each other: weak airflow, overheating and damage. Go back and test each stage alone."
        }) { Text("Run both") }
        Spacer(Modifier.width(4.dp))
        OutlinedButton(onClick = { round++ }) { Text("New fan") }
    }
    Explain(msg, if (running == 3 && !(s1ok && s2ok)) Palette.Bad else Color.Unspecified)
    Explain("Stage 1 is the incoming stage. Test each stage on its own by turning the other stage off (its own breaker, selector or run relay), with the starter isolated while you change it. Swap phases ONLY after isolating and proving dead.")
}
