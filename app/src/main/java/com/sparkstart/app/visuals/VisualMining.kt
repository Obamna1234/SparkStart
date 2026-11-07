package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.ui.Palette
import kotlin.random.Random

private val Steel = Color(0xFF90A4AE)
private val Orange = Color(0xFFFF9800)

@Composable
internal fun PumpSharedSim() {
    // 0 = pump in the jumbo's own pump outlet, 1 = pump on a separate pump starter
    var mode by remember { mutableIntStateOf(1) }
    var fault by remember { mutableStateOf(false) }
    var tripped by remember(mode, fault) { mutableStateOf(false) }
    androidx.compose.runtime.LaunchedEffect(mode, fault) {
        if (fault && mode == 0) {
            kotlinx.coroutines.delay(1500)
            tripped = true
        }
    }
    val live = fault && !tripped
    val phase = rememberFlow(if (live) 0.35f else 0.1f)
    val ink = ink()
    PillRow(listOf("Pump in the JUMBO's pump outlet", "Pump on a SEPARATE pump starter"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(260.dp)) {
        val W = size.width
        val H = size.height
        val floorY = H * 0.8f
        val waterTop = floorY - 8.dp.toPx()
        val waterLive = live && mode == 1
        // floor + water across the face area
        drawRect(
            if (waterLive) Palette.Bad.copy(alpha = 0.35f) else Palette.Water.copy(alpha = 0.4f),
            Offset(W * 0.25f, waterTop), Size(W * 0.75f, H - waterTop)
        )
        drawLine(Color(0xFF8D6E63), Offset(0f, floorY), Offset(W * 0.25f, floorY), 3.dp.toPx())
        label(if (waterLive) "WATER ENERGISED" else "water at the face", W * 0.62f, H - 6.dp.toPx(), if (waterLive) Palette.Bad else ink, 10f)
        // jumbo box (supply)
        box(Offset(W * 0.02f, H * 0.06f), Size(W * 0.17f, H * 0.22f), Color(0xFFECEFF1), ink)
        label("jumbo", W * 0.105f, H * 0.15f, Color.Black, 10f)
        label("box", W * 0.105f, H * 0.15f + 12.dp.toPx(), Color.Black, 10f)
        // jumbo with stabiliser legs into the water
        val jL = W * 0.3f
        val jR = W * 0.62f
        val jT = H * 0.36f
        val jB = H * 0.62f
        val frameLive = waterLive
        drawRect(if (frameLive) Orange.copy(alpha = 0.5f) else Color(0xFFFFD54F).copy(alpha = 0.6f), Offset(jL, jT), Size(jR - jL, jB - jT))
        drawRect(ink, Offset(jL, jT), Size(jR - jL, jB - jT), style = Stroke(2.dp.toPx()))
        label("JUMBO", (jL + jR) / 2, (jT + jB) / 2 + 4.dp.toPx(), Color.Black, 13f)
        val legL = jL + 10.dp.toPx()
        val legR = jR - 10.dp.toPx()
        drawLine(Steel, Offset(legL, jB), Offset(legL, floorY + 4.dp.toPx()), 5.dp.toPx())
        drawLine(Steel, Offset(legR, jB), Offset(legR, floorY + 4.dp.toPx()), 5.dp.toPx())
        label("stabiliser legs", (jL + jR) / 2, jB + 14.dp.toPx(), ink, 9f)
        // jumbo trailing cable
        val jbOut = Offset(W * 0.19f, H * 0.2f)
        val jIn = Offset(jL, jT + 10.dp.toPx())
        drawLine(Color(0xFF424242), jbOut, jIn, 5.dp.toPx())
        // separate pump starter (mode 1)
        val psL = W * 0.74f
        val psT = H * 0.04f
        if (mode == 1) {
            box(Offset(psL, psT), Size(W * 0.22f, H * 0.18f), Color(0xFFECEFF1), ink)
            label("separate", psL + W * 0.11f, psT + 14.dp.toPx(), Color.Black, 9f)
            label("pump starter", psL + W * 0.11f, psT + 26.dp.toPx(), Color.Black, 9f)
        }
        // pump in the water
        val pX = W * 0.86f
        val pY = floorY + 10.dp.toPx()
        drawRect(Steel, Offset(pX - 12.dp.toPx(), pY - 18.dp.toPx()), Size(24.dp.toPx(), 30.dp.toPx()))
        label("pump", pX, pY - 22.dp.toPx(), ink, 10f)
        val pumpCable: List<Offset> = if (mode == 0) {
            listOf(Offset(jR, jT + 12.dp.toPx()), Offset(pX, jT + 12.dp.toPx()), Offset(pX, pY - 18.dp.toPx()))
        } else {
            listOf(Offset(psL + W * 0.11f, psT + H * 0.18f), Offset(pX, psT + H * 0.18f + 30.dp.toPx()), Offset(pX, pY - 18.dp.toPx()))
        }
        polyline(pumpCable, Color(0xFF424242), 4.dp.toPx())
        if (mode == 0) label("jumbo's pump outlet", (jR + pX) / 2, jT + 4.dp.toPx(), ink, 9f)
        // operator standing in water next to the rig
        val personX = jR + 22.dp.toPx()
        stickPerson(Offset(personX, floorY), 52.dp.toPx(), if (waterLive) Palette.Bad else ink)
        if (fault) label("⚡ pump fault", pX, floorY - 34.dp.toPx(), Palette.Bad, 10f)
        if (live && mode == 1) {
            // loop: pump -> water -> stabiliser legs -> jumbo frame -> jumbo trailing cable earth -> jumbo box
            val loop = listOf(
                Offset(pX - 12.dp.toPx(), pY), Offset(legR, floorY + 4.dp.toPx()), Offset(legR, jB),
                Offset(legL, jB), Offset(jL, jT + 10.dp.toPx()), jbOut
            )
            dotsAlong(loop, phase, 14, Palette.Bad, 4.dp.toPx())
            label("fault current loops through water,", W * 0.5f, H * 0.29f, Palette.Bad, 10f)
            label("legs, jumbo frame & jumbo cable", W * 0.5f, H * 0.29f + 12.dp.toPx(), Palette.Bad, 10f)
        }
        if (live && mode == 0) {
            // fault goes straight back on the pump cable earth -> jumbo -> jumbo cable -> jumbo box
            dotsAlong(pumpCable.reversed() + listOf(jIn, jbOut), phase, 12, Orange, 3.5.dp.toPx())
            label("fault returns on the earth core", W * 0.5f, H * 0.29f, Orange, 10f)
        }
        if (tripped) label("✅ jumbo box protection TRIPPED", W * 0.5f, H * 0.29f, Palette.Good, 11f)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { fault = !fault }) { Text(if (fault) "Clear the fault" else "Make a pump fault ⚡") }
    }
    Explain(
        when {
            !fault && mode == 0 -> "Pump plugged into the jumbo's own pump outlet: jumbo and pump share ONE supply, ONE earth system and ONE set of protection at the jumbo box. Their frames are bonded together."
            !fault -> "Pump on a separate starter: the jumbo and pump are on two different circuits with different earth paths, but they're standing in the same water."
            mode == 0 && !tripped -> "Pump fault: the fault current goes back along the pump cable's earth core, through the jumbo and up its trailing cable, to the jumbo box…"
            mode == 0 -> "…where the protection sees it and trips. The jumbo and pump are bonded together, so there's no voltage difference between them for the operator to bridge."
            else -> "Pump fault: the current has another way home. It goes out of the pump, through the water, up the jumbo's stabiliser legs, through the frame and back along the jumbo's trailing cable. The pump's protection SHOULD trip. But if it doesn't (faulty relay, leakage below its trip setting, or a high-resistance path through the water), the water and the jumbo stay energised, with the operator standing in it."
        },
        if (fault && mode == 1) Palette.Bad else Color.Unspecified
    )
    Explain("This shows the physics: water conducts, the stabiliser legs give a path to ground, and two separate earth systems in the same water can push current through it. Your site's exact rule, and what it calls this, comes from your supervisor and site electrical standards.")
}

@Composable
internal fun PilotCoreSim() {
    // 0 ok, 1 earth broken, 2 pilot damaged, 3 unplugged
    var state by remember { mutableIntStateOf(0) }
    val ok = state == 0
    val phase = rememberFlow(if (ok) 0.2f else 0f)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(200.dp)) {
        val W = size.width
        val H = size.height
        val l = W * 0.24f
        val r = W * 0.8f
        val grey = Color(0xFF9E9E9E)
        box(Offset(W * 0.02f, H * 0.1f), Size(W * 0.2f, H * 0.8f), Color(0xFFECEFF1), ink)
        label("supply", W * 0.12f, H * 0.22f, Color.Black, 10f)
        label("earth", W * 0.12f, H * 0.62f, Color.Black, 10f)
        label("continuity", W * 0.12f, H * 0.62f + 12.dp.toPx(), Color.Black, 10f)
        label("relay", W * 0.12f, H * 0.62f + 24.dp.toPx(), Color.Black, 10f)
        box(Offset(r, H * 0.1f), Size(W * 0.18f, H * 0.8f), Color(0xFFFFD54F).copy(alpha = 0.6f), ink)
        label("machine", r + W * 0.09f, H * 0.5f, Color.Black, 11f)
        val ys = listOf(0.2f, 0.3f, 0.4f, 0.58f, 0.74f).map { H * it }
        val phaseCols = listOf(Palette.Active, Color(0xFF9E9E9E), Palette.Neutral)
        for (k in 0..2) drawLine(if (ok) phaseCols[k] else grey, Offset(l, ys[k]), Offset(r, ys[k]), 4.dp.toPx())
        // earth core
        if (state == 1) {
            earthLine(Offset(l, ys[3]), Offset(W * 0.48f, ys[3]), 4.dp.toPx())
            earthLine(Offset(W * 0.55f, ys[3]), Offset(r, ys[3]), 4.dp.toPx())
            label("✕", W * 0.515f, ys[3] + 5.dp.toPx(), Palette.Bad, 16f)
        } else if (state != 3) {
            earthLine(Offset(l, ys[3]), Offset(r, ys[3]), 4.dp.toPx())
        }
        // pilot core
        if (state == 2) {
            drawLine(Orange, Offset(l, ys[4]), Offset(W * 0.6f, ys[4]), 3.dp.toPx())
            drawLine(Orange, Offset(W * 0.66f, ys[4]), Offset(r, ys[4]), 3.dp.toPx())
            label("✕", W * 0.63f, ys[4] + 5.dp.toPx(), Palette.Bad, 16f)
        } else if (state != 3) {
            drawLine(Orange, Offset(l, ys[4]), Offset(r, ys[4]), 3.dp.toPx())
        }
        if (state == 3) {
            label("plug pulled out", (l + r) / 2, ys[3] + 10.dp.toPx(), Palette.Bad, 12f)
        }
        label("pilot", l + 4.dp.toPx(), ys[4] - 5.dp.toPx(), Orange, 9f, Paint.Align.LEFT)
        label("earth", l + 4.dp.toPx(), ys[3] - 6.dp.toPx(), Palette.Earth, 9f, Paint.Align.LEFT)
        // termination at machine end joining pilot to earth
        drawLine(ink, Offset(r - 6.dp.toPx(), ys[4]), Offset(r - 6.dp.toPx(), ys[3]), 2.dp.toPx())
        if (ok) {
            dotsAlong(listOf(Offset(l, ys[4]), Offset(r - 6.dp.toPx(), ys[4]), Offset(r - 6.dp.toPx(), ys[3]), Offset(l, ys[3])), phase, 10, Orange, 3.dp.toPx())
        }
        label(if (ok) "POWER ON" else "TRIPPED: power OFF", W * 0.52f, H * 0.1f, if (ok) Palette.Good else Palette.Bad, 12f)
    }
    Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = { state = 1 }) { Text("Break earth", fontSize = 12.sp) }
        Spacer(Modifier.width(4.dp))
        OutlinedButton(onClick = { state = 2 }) { Text("Cut pilot", fontSize = 12.sp) }
        Spacer(Modifier.width(4.dp))
        OutlinedButton(onClick = { state = 3 }) { Text("Unplug", fontSize = 12.sp) }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { state = 0 }) { Text("Repair & reset") }
    }
    Explain(
        when (state) {
            0 -> "The relay at the supply end sends a small monitoring signal down the PILOT core, through a termination at the machine end, and back along the EARTH core (orange dots). While that loop is healthy, power is allowed on."
            1 -> "The earth core broke (e.g. the cable got run over or pulled). The monitoring loop is broken, so the relay trips the power. A machine with no earth must NEVER stay energised, because a fault would make the whole frame live."
            2 -> "Pilot damaged: same result. The relay can't confirm the earth is there, so it fails safe and cuts the power."
            else -> "Pulling the plug breaks the pilot loop, so power is cut. Restrained plugs are designed so you don't separate live pins under load. Check how YOUR site's plugs and interlocks work."
        },
        if (state == 0) Color.Unspecified else Palette.Bad
    )
}

@Composable
internal fun NerSim() {
    var mode by remember { mutableIntStateOf(1) }
    var fault by remember { mutableStateOf(false) }
    val phase = rememberFlow(if (fault) (if (mode == 0) 0.9f else 0.15f) else 0f)
    val ink = ink()
    PillRow(listOf("Solidly earthed", "Earthed through a resistor (NER)"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(210.dp)) {
        val W = size.width
        val H = size.height
        val sx = W * 0.15f
        val gY = H * 0.9f
        val lineY = H * 0.2f
        drawCircle(ink, 16.dp.toPx(), Offset(sx, H * 0.35f), style = Stroke(2.dp.toPx()))
        label("transformer", sx, H * 0.08f, ink, 10f)
        label("star point", sx + 20.dp.toPx(), H * 0.52f, ink, 9f, Paint.Align.LEFT)
        // star point down to earth
        if (mode == 0) {
            drawLine(Palette.Earth, Offset(sx, H * 0.43f), Offset(sx, gY), 3.dp.toPx())
        } else {
            drawLine(Palette.Earth, Offset(sx, H * 0.43f), Offset(sx, H * 0.55f), 3.dp.toPx())
            val zig = Path().apply {
                moveTo(sx, H * 0.55f)
                for (k in 1..6) lineTo(sx + (if (k % 2 == 0) -8 else 8).dp.toPx(), H * 0.55f + k * 6.dp.toPx())
                lineTo(sx, H * 0.55f + 42.dp.toPx())
            }
            drawPath(zig, Orange, style = Stroke(3.dp.toPx()))
            label("NER", sx + 14.dp.toPx(), H * 0.67f, Orange, 11f, Paint.Align.LEFT)
            drawLine(Palette.Earth, Offset(sx, H * 0.55f + 42.dp.toPx()), Offset(sx, gY), 3.dp.toPx())
        }
        drawLine(Palette.Active, Offset(sx + 16.dp.toPx(), H * 0.3f), Offset(sx + 16.dp.toPx(), lineY), 3.dp.toPx())
        drawLine(Palette.Active, Offset(sx + 16.dp.toPx(), lineY), Offset(W * 0.8f, lineY), 3.dp.toPx())
        // machine frame
        val mL = W * 0.65f
        drawRect(if (fault) Palette.Bad.copy(alpha = if (mode == 0) 0.4f else 0.15f) else Steel.copy(alpha = 0.4f), Offset(mL, H * 0.3f), Size(W * 0.3f, H * 0.4f))
        drawRect(ink, Offset(mL, H * 0.3f), Size(W * 0.3f, H * 0.4f), style = Stroke(2.dp.toPx()))
        label("machine frame", mL + W * 0.15f, H * 0.5f, ink, 10f)
        earthLine(Offset(mL + W * 0.15f, H * 0.7f), Offset(mL + W * 0.15f, gY), 3.dp.toPx())
        drawLine(Palette.Earth, Offset(sx, gY), Offset(mL + W * 0.15f, gY), 3.dp.toPx())
        label("earth system", W * 0.45f, gY + 14.dp.toPx(), Palette.Earth, 9f)
        if (fault) {
            label("⚡ phase touches frame", W * 0.8f, H * 0.28f, Palette.Bad, 10f)
            val path = listOf(Offset(sx + 16.dp.toPx(), lineY), Offset(W * 0.8f, lineY), Offset(W * 0.8f, H * 0.35f), Offset(mL + W * 0.15f, H * 0.7f), Offset(mL + W * 0.15f, gY), Offset(sx, gY), Offset(sx, H * 0.43f))
            dotsAlong(path, phase, if (mode == 0) 26 else 8, Palette.Bad, if (mode == 0) 5.dp.toPx() else 3.dp.toPx())
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { fault = !fault }) { Text(if (fault) "Clear fault" else "Phase-to-frame fault ⚡") }
    }
    Explain(
        when {
            !fault -> "Mine power systems are often earthed differently from a house. Many connect the transformer's star point to earth through a neutral earthing resistor (NER)."
            mode == 0 -> "Solidly earthed: an earth fault can make a VERY large current. That trips the protection fast, but the big current flowing through the earth path can also raise the voltage on the machine frame (touch voltage) while it lasts."
            else -> "With an NER: the resistor limits earth-fault current to a small, set value chosen by the mine's electrical engineer. Lower fault current = lower touch voltage on frames and less damage. Because the fault current is small, SENSITIVE earth-leakage protection is used to detect it and trip."
        }
    )
    Explain("Exact NER ratings and trip settings are site-specific and engineered. You'll find them in your site's electrical documentation, not here.")
}

@Composable
internal fun BackfeedSim() {
    var mode by remember { mutableIntStateOf(0) }
    val phase = rememberFlow(0.25f)
    val ink = ink()
    PillRow(listOf("Proper changeover switch", "Generator plugged into a power point"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(220.dp)) {
        val W = size.width
        val H = size.height
        val grey = Color(0xFF9E9E9E)
        val bad = mode == 1
        // pole + transformer + worker
        drawLine(Color(0xFF6D4C41), Offset(W * 0.12f, H * 0.95f), Offset(W * 0.12f, H * 0.1f), 6.dp.toPx())
        drawCircle(ink, 12.dp.toPx(), Offset(W * 0.2f, H * 0.25f), style = Stroke(2.dp.toPx()))
        label("street", W * 0.2f, H * 0.08f, ink, 9f)
        label("transformer", W * 0.2f, H * 0.08f + 11.dp.toPx(), ink, 9f)
        stickPerson(Offset(W * 0.06f, H * 0.62f), 48.dp.toPx(), if (bad) Palette.Bad else ink)
        label("lineworker", W * 0.06f, H * 0.68f, ink, 9f)
        label("(thinks it's off)", W * 0.06f, H * 0.68f + 11.dp.toPx(), ink, 9f)
        // service line
        drawLine(if (bad) Palette.Active else grey, Offset(W * 0.22f, H * 0.3f), Offset(W * 0.55f, H * 0.3f), 3.dp.toPx())
        // house
        val hL = W * 0.55f
        drawRect(ink, Offset(hL, H * 0.25f), Size(W * 0.4f, H * 0.65f), style = Stroke(2.dp.toPx()))
        label("house", hL + W * 0.2f, H * 0.33f, ink, 11f)
        // generator
        box(Offset(W * 0.7f, H * 0.62f), Size(W * 0.18f, H * 0.2f), Color(0xFFFFD54F), ink)
        label("generator", W * 0.79f, H * 0.74f, Color.Black, 10f)
        if (bad) {
            dotsAlong(listOf(Offset(W * 0.7f, H * 0.7f), Offset(hL + 6.dp.toPx(), H * 0.7f), Offset(hL + 6.dp.toPx(), H * 0.3f), Offset(W * 0.22f, H * 0.3f), Offset(W * 0.2f, H * 0.25f)), phase, 12, Palette.Bad, 4.dp.toPx())
            label("230 V fed BACKWARDS", W * 0.38f, H * 0.25f, Palette.Bad, 10f)
            label("transformer steps it UP to thousands of volts!", W * 0.45f, H * 0.5f, Palette.Bad, 10f)
        } else {
            label("changeover switch: grid", hL + W * 0.2f, H * 0.48f, Palette.Good, 10f)
            label("is disconnected first", hL + W * 0.2f, H * 0.48f + 12.dp.toPx(), Palette.Good, 10f)
            dotsAlong(listOf(Offset(W * 0.7f, H * 0.7f), Offset(hL + 30.dp.toPx(), H * 0.7f)), phase, 4, Palette.Current, 4.dp.toPx())
        }
    }
    Explain(
        if (mode == 1) "Plugging a generator into a power point with a male-to-male lead pushes power BACKWARDS out of the house, into the street, and through the transformer, which steps 230 V UP to high voltage. A lineworker who thinks the line is dead can be killed. That lead also has live exposed pins. Never, ever do this."
        else "A proper changeover switch (or interlocked inlet) means the house can be on the grid OR the generator, never both. The grid side is disconnected before the generator connects.",
        if (mode == 1) Palette.Bad else Color.Unspecified
    )
}

@Composable
internal fun HalfSplitSim() {
    var round by remember { mutableIntStateOf(0) }
    val faultAfter = remember(round) { Random.nextInt(1, 8) } // fault between point faultAfter and faultAfter+1
    var tested by remember(round) { mutableStateOf(mapOf<Int, Boolean>()) }
    val found = (1..7).any { i -> tested[i] == true && tested[i + 1] == false } ||
        (tested[1] == false) || (tested[8] == true)
    Text("Supply ➜ 8 connection points ➜ load (dead). Tap a point to test for voltage there.", style = MaterialTheme.typography.bodyMedium)
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Text("🔌", fontSize = 20.sp)
        for (i in 1..8) {
            val res = tested[i]
            Box(
                Modifier.size(34.dp).clip(CircleShape)
                    .background(
                        when (res) {
                            true -> Palette.Good
                            false -> Palette.Bad
                            null -> MaterialTheme.colorScheme.surface
                        }
                    )
                    .clickable(enabled = !found) { tested = tested + (i to (i <= faultAfter)) },
                contentAlignment = Alignment.Center
            ) {
                Text("$i", fontWeight = FontWeight.Bold, color = if (res == null) MaterialTheme.colorScheme.onSurface else Color.White)
            }
        }
        Text("💡", fontSize = 20.sp)
    }
    Text("Green = voltage present, red = no voltage. Tests used: ${tested.size}", style = MaterialTheme.typography.labelLarge)
    if (found) {
        val a = (1..7).firstOrNull { i -> tested[i] == true && tested[i + 1] == false }
        Explain(
            if (a != null) "Found it! The fault (open circuit) is between point $a and point ${a + 1}. You used ${tested.size} tests."
            else "Found the area at one end. Check there.",
            Palette.Good
        )
        Button(onClick = { round++ }) { Text("New fault") }
    }
    Explain("Smart method = HALF-SPLITTING: test in the middle (point 4). Voltage there? The fault is after it, so test 6 next. None? It's before, so test 2. Each test halves the search. 8 points takes 3–4 tests instead of up to 8.")
    Explain("In real life this test is done LIVE, which brings its own hazards. Only competent, authorised people do live testing, using the correct PPE and procedures. Where you can, find faults with the circuit isolated, using continuity and insulation tests.")
}
