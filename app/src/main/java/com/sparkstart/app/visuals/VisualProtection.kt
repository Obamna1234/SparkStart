package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sparkstart.app.ui.Palette
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
internal fun BodyCurrentSim() {
    var ma by remember { mutableFloatStateOf(5f) }
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(70.dp)) {
        val zones = listOf(0f to Palette.Good, 1f to Color(0xFFFDD835), 10f to Color(0xFFFF9800), 30f to Palette.Bad, 50f to Color(0xFF6A1B9A))
        val maxMa = 100f
        val top = 10.dp.toPx()
        val h = 24.dp.toPx()
        zones.forEachIndexed { i, (start, c) ->
            val end = if (i < zones.lastIndex) zones[i + 1].first else maxMa
            drawRect(c, Offset(size.width * start / maxMa, top), Size(size.width * (end - start) / maxMa, h))
        }
        val x = size.width * ma / maxMa
        drawLine(ink, Offset(x, 0f), Offset(x, top + h + 6.dp.toPx()), 3.dp.toPx())
        label("${ma.roundToInt()} mA", x.coerceIn(30.dp.toPx(), size.width - 30.dp.toPx()), top + h + 22.dp.toPx(), ink, 12f)
        label("30 mA", size.width * 0.3f, top - 2.dp.toPx(), ink, 9f)
    }
    LabeledSlider("Current through your body", ma, 0f..100f) { ma = it }
    val m = ma.roundToInt()
    Explain(
        when {
            m < 1 -> "Under about 1 mA: you probably won't even feel it."
            m < 10 -> "About 1–10 mA: a tingle up to a painful shock. You can usually still let go."
            m < 30 -> "About 10–30 mA: your muscles can lock on. You may NOT be able to let go of the thing that's shocking you."
            m < 50 -> "About 30 mA and up: breathing can be affected. This is why safety switches (RCDs) are designed to trip by 30 mA."
            else -> "About 50 mA and up: your heart can go into fibrillation (it quivers instead of pumping). This can kill in under a second."
        },
        if (m >= 30) Palette.Bad else Color.Unspecified
    )
    Explain("For comparison, a kettle draws about 10,000 mA (10 A). It's not the voltage that kills, it's the CURRENT through your body, and it only takes a tiny bit. These numbers are rough guides. Effects vary with time, path through the body, wet skin and more.")
}

@Composable
internal fun SwitchActiveSim() {
    var where by remember { mutableIntStateOf(0) }
    var on by remember { mutableStateOf(false) }
    val ink = ink()
    val dead = Color(0xFF9E9E9E)
    PillRow(listOf("Switch in ACTIVE ✅", "Switch in NEUTRAL ❌"), where) { where = it }
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        val l = size.width * 0.12f
        val lx = size.width * 0.8f
        val ty = size.height * 0.25f
        val by = size.height * 0.75f
        val sx = size.width * 0.45f
        val sw = 4.dp.toPx()
        val gap = 24.dp.toPx()
        val inActive = where == 0
        // colours of each segment
        val topRight = if (on || !inActive) Palette.Active else dead
        val lampLive = !on && !inActive
        val botRight = when {
            on -> Palette.Neutral
            inActive -> dead
            else -> Palette.Active // neutral-switched: lamp side is at active potential
        }
        // top (active)
        if (inActive) {
            drawLine(Palette.Active, Offset(l, ty), Offset(sx - gap / 2, ty), sw)
            drawLine(topRight, Offset(sx + gap / 2, ty), Offset(lx, ty), sw)
            if (on) drawLine(Palette.Active, Offset(sx - gap / 2, ty), Offset(sx + gap / 2, ty), sw)
            else drawLine(Palette.Active, Offset(sx - gap / 2, ty), Offset(sx + gap / 2, ty - 18.dp.toPx()), sw)
        } else {
            drawLine(Palette.Active, Offset(l, ty), Offset(lx, ty), sw)
        }
        // bottom (neutral)
        if (!inActive) {
            drawLine(Palette.Neutral, Offset(l, by), Offset(sx - gap / 2, by), sw)
            drawLine(botRight, Offset(sx + gap / 2, by), Offset(lx, by), sw)
            if (on) drawLine(Palette.Neutral, Offset(sx - gap / 2, by), Offset(sx + gap / 2, by), sw)
            else drawLine(botRight, Offset(sx + gap / 2, by), Offset(sx - gap / 2, by - 18.dp.toPx()), sw)
        } else {
            drawLine(Palette.Neutral, Offset(l, by), Offset(lx, by), sw)
        }
        drawLine(if (on) ink else if (lampLive) Palette.Active else dead, Offset(lx, ty), Offset(lx, by), sw)
        lamp(Offset(lx, (ty + by) / 2), 18.dp.toPx(), if (on) 1f else 0f, ink)
        if (lampLive) drawCircle(Palette.Bad, 26.dp.toPx(), Offset(lx, (ty + by) / 2), style = Stroke(3.dp.toPx()))
        label("A 230 V", l, ty - 10.dp.toPx(), Palette.Active, 11f, Paint.Align.LEFT)
        label("N", l, by + 18.dp.toPx(), Palette.Neutral, 11f, Paint.Align.LEFT)
        label("switch", sx, if (inActive) ty + 22.dp.toPx() else by + 22.dp.toPx(), ink, 10f)
        if (lampLive) label("STILL LIVE!", lx, by + 22.dp.toPx(), Palette.Bad, 12f)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { on = !on }) { Text(if (on) "Switch OFF" else "Switch ON") }
    }
    Explain(
        when {
            on -> "Light is on. Either way works when it's ON. The difference shows up when you switch it OFF."
            where == 0 -> "Switch in the active: when it's off, everything past the switch (grey) is dead. Someone changing the globe is safe from that circuit."
            else -> "Switch in the neutral: the light goes off, BUT the lampholder is still connected to 230 V through the active (red). Someone changing the globe could get a shock. That's why switches must break the ACTIVE."
        },
        if (!on && where == 1) Palette.Bad else Color.Unspecified
    )
}

@Composable
internal fun MenFaultSim() {
    // 0 normal, 1 fault, 2 tripped
    var mode by remember { mutableIntStateOf(0) }
    var menRemoved by remember { mutableStateOf(false) }
    LaunchedEffect(mode, menRemoved) {
        if (mode == 1 && !menRemoved) {
            delay(1800)
            mode = 2
        }
    }
    val speed = when {
        mode == 2 -> 0f
        mode == 1 && menRemoved -> 0.05f
        mode == 1 -> 0.6f
        else -> 0.15f
    }
    val phase = rememberFlow(speed)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(250.dp)) {
        val W = size.width
        val H = size.height
        val sw = 3.dp.toPx()
        val txX = W * 0.07f
        val aY = H * 0.18f
        val nY = H * 0.5f
        val eY = H * 0.74f
        val gY = H * 0.93f
        val barX = W * 0.46f
        val appL = W * 0.72f
        val appR = W * 0.95f
        val brkX = W * 0.32f
        // ground
        drawLine(Color(0xFF8D6E63), Offset(0f, gY), Offset(W, gY), 2.dp.toPx())
        // transformer
        drawCircle(ink, 12.dp.toPx(), Offset(txX, (aY + nY) / 2 - 8.dp.toPx()), style = Stroke(2.dp.toPx()))
        drawCircle(ink, 12.dp.toPx(), Offset(txX, (aY + nY) / 2 + 8.dp.toPx()), style = Stroke(2.dp.toPx()))
        label("street", txX, H * 0.08f, ink, 9f)
        label("transformer", txX, H * 0.08f + 11.dp.toPx(), ink, 9f)
        // active + neutral
        val tripped = mode == 2
        drawLine(Palette.Active, Offset(txX, aY), Offset(brkX, aY), sw)
        drawLine(if (tripped) Color(0xFF9E9E9E) else Palette.Active, Offset(brkX + 14.dp.toPx(), aY), Offset(appL + 20.dp.toPx(), aY), sw)
        drawLine(Palette.Neutral, Offset(txX, nY), Offset(appL + 20.dp.toPx(), nY), sw)
        drawLine(Palette.Active, Offset(appL + 20.dp.toPx(), aY), Offset(appL + 20.dp.toPx(), aY + 14.dp.toPx()), sw)
        drawLine(Palette.Neutral, Offset(appL + 20.dp.toPx(), nY), Offset(appL + 20.dp.toPx(), nY - 14.dp.toPx()), sw)
        // breaker
        drawRect(if (tripped) Palette.Bad else ink, Offset(brkX, aY - 9.dp.toPx()), Size(14.dp.toPx(), 18.dp.toPx()), style = Stroke(2.dp.toPx()))
        label(if (tripped) "TRIPPED" else "breaker", brkX + 7.dp.toPx(), aY - 14.dp.toPx(), if (tripped) Palette.Bad else ink, 10f)
        // transformer neutral earthed
        drawLine(Palette.Neutral, Offset(txX, nY), Offset(txX, gY), 2.dp.toPx())
        // switchboard outline
        drawRect(ink.copy(alpha = 0.35f), Offset(W * 0.26f, H * 0.06f), Size(W * 0.3f, H * 0.78f), style = Stroke(1.5.dp.toPx(), pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 8f))))
        label("switchboard", W * 0.41f, H * 0.82f, ink, 9f)
        // N bar & E bar
        drawRect(Palette.Neutral, Offset(barX - 10.dp.toPx(), nY - 4.dp.toPx()), Size(20.dp.toPx(), 8.dp.toPx()))
        drawRect(Palette.Earth, Offset(barX - 10.dp.toPx(), eY - 4.dp.toPx()), Size(20.dp.toPx(), 8.dp.toPx()))
        label("N bar", barX + 14.dp.toPx(), nY + 14.dp.toPx(), ink, 9f, Paint.Align.LEFT)
        label("E bar", barX + 14.dp.toPx(), eY + 14.dp.toPx(), ink, 9f, Paint.Align.LEFT)
        // MEN link
        if (!menRemoved) {
            drawLine(Color(0xFFFFB300), Offset(barX, nY), Offset(barX, eY), 4.dp.toPx())
            label("MEN link", barX - 6.dp.toPx(), (nY + eY) / 2 + 4.dp.toPx(), Color(0xFFFFB300), 10f, Paint.Align.RIGHT)
        } else {
            drawLine(Color(0xFFFFB300), Offset(barX, nY), Offset(barX, nY + 12.dp.toPx()), 4.dp.toPx())
            drawLine(Color(0xFFFFB300), Offset(barX, eY - 12.dp.toPx()), Offset(barX, eY), 4.dp.toPx())
            label("MISSING", barX - 6.dp.toPx(), (nY + eY) / 2 + 4.dp.toPx(), Palette.Bad, 10f, Paint.Align.RIGHT)
        }
        // earth stake
        earthLine(Offset(barX, eY), Offset(barX, gY), 3.dp.toPx())
        label("earth stake", barX + 6.dp.toPx(), gY - 6.dp.toPx(), ink, 9f, Paint.Align.LEFT)
        // appliance
        val caseLive = mode == 1 && menRemoved
        drawRect(if (caseLive) Palette.Bad.copy(alpha = 0.25f) else Color(0xFFB0BEC5).copy(alpha = 0.4f), Offset(appL, H * 0.12f), Size(appR - appL, H * 0.48f))
        drawRect(ink, Offset(appL, H * 0.12f), Size(appR - appL, H * 0.48f), style = Stroke(2.dp.toPx()))
        label("metal", (appL + appR) / 2, H * 0.3f, ink, 10f)
        label("kettle", (appL + appR) / 2, H * 0.3f + 12.dp.toPx(), ink, 10f)
        // earth wire from appliance case to E bar
        val caseEx = (appL + appR) / 2
        earthLine(Offset(caseEx, H * 0.6f), Offset(caseEx, eY), 3.dp.toPx())
        earthLine(Offset(caseEx, eY), Offset(barX, eY), 3.dp.toPx())
        if (mode >= 1) label("⚡ fault: active touching the case", (appL + appR) / 2 - 10.dp.toPx(), H * 0.07f, Palette.Bad, 10f)
        if (caseLive) stickPerson(Offset(appR - 4.dp.toPx(), gY), 50.dp.toPx(), Palette.Bad)

        // current path
        val normal = listOf(
            Offset(txX, aY), Offset(appL + 20.dp.toPx(), aY), Offset(appL + 20.dp.toPx(), aY + 14.dp.toPx()),
            Offset(appL + 20.dp.toPx(), nY - 14.dp.toPx()), Offset(appL + 20.dp.toPx(), nY), Offset(txX, nY)
        )
        val fault = listOf(
            Offset(txX, aY), Offset(appL + 20.dp.toPx(), aY), Offset(caseEx, H * 0.35f), Offset(caseEx, eY),
            Offset(barX, eY), Offset(barX, nY), Offset(txX, nY)
        )
        val faultNoMen = listOf(
            Offset(txX, aY), Offset(appL + 20.dp.toPx(), aY), Offset(caseEx, H * 0.35f), Offset(caseEx, eY),
            Offset(barX, eY), Offset(barX, gY), Offset(txX, gY), Offset(txX, nY)
        )
        when {
            mode == 0 -> dotsAlong(normal, phase, 16, Palette.Current, 4.dp.toPx())
            mode == 1 && !menRemoved -> dotsAlong(fault, phase, 22, Palette.Bad, 5.dp.toPx())
            mode == 1 -> dotsAlong(faultNoMen, phase, 5, Palette.Bad, 4.dp.toPx())
            else -> {}
        }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        if (mode == 0) Button(onClick = { mode = 1 }) { Text("Make a fault ⚡") }
        else Button(onClick = { mode = 0 }) { Text("Fix fault & reset") }
        Spacer(Modifier.width(8.dp))
        OutlinedButton(onClick = { menRemoved = !menRemoved; if (mode == 2) mode = 1 }) {
            Text(if (menRemoved) "Put MEN link back" else "Remove MEN link?")
        }
    }
    Explain(
        when {
            mode == 0 -> "Normal: current goes out on the active (red), through the kettle, and back on the neutral (blue)."
            mode == 1 && !menRemoved -> "FAULT: the active touches the metal case. Current rushes down the earth wire → earth bar → MEN link → neutral → back to the transformer. That low-resistance loop makes a HUGE current…"
            mode == 2 -> "…so the circuit breaker trips almost instantly. The case is made safe. This is exactly why the earth wire and the MEN link matter."
            else -> "WITHOUT the MEN link the fault current has to go through the soil to get back to the transformer. The soil has high resistance, so the current is too small to trip the breaker. The kettle case stays LIVE and anyone touching it is in danger."
        },
        if (mode == 1 && menRemoved) Palette.Bad else Color.Unspecified
    )
}

@Composable
internal fun RcdSim() {
    var scen by remember { mutableIntStateOf(0) }
    var tripped by remember(scen) { mutableStateOf(false) }
    val leakMa = when (scen) {
        1 -> 40
        else -> 0
    }
    LaunchedEffect(scen) {
        if (scen == 1) {
            delay(900)
            tripped = true
        }
    }
    val phase = rememberFlow(if (tripped) 0f else 0.12f)
    val ink = ink()
    PillRow(listOf("Normal", "Person touches active (to earth)", "Person between A and N"), scen) { scen = it }
    Canvas(Modifier.fillMaxWidth().height(220.dp)) {
        val W = size.width
        val H = size.height
        val sw = 3.dp.toPx()
        val aY = H * 0.25f
        val nY = H * 0.45f
        val rL = W * 0.2f
        val rR = W * 0.42f
        val grey = Color(0xFF9E9E9E)
        drawLine(Palette.Active, Offset(0f, aY), Offset(rL, aY), sw)
        drawLine(Palette.Neutral, Offset(0f, nY), Offset(rL, nY), sw)
        drawLine(if (tripped) grey else Palette.Active, Offset(rR, aY), Offset(W * 0.88f, aY), sw)
        drawLine(if (tripped) grey else Palette.Neutral, Offset(rR, nY), Offset(W * 0.88f, nY), sw)
        drawLine(if (tripped) grey else ink, Offset(W * 0.88f, aY), Offset(W * 0.88f, nY), sw)
        lamp(Offset(W * 0.88f, (aY + nY) / 2), 13.dp.toPx(), if (tripped) 0f else 1f, ink)
        // RCD box with core
        drawRect(if (tripped) Palette.Bad.copy(alpha = 0.2f) else Color(0xFFECEFF1).copy(alpha = 0.6f), Offset(rL, H * 0.1f), Size(rR - rL, H * 0.5f))
        drawRect(ink, Offset(rL, H * 0.1f), Size(rR - rL, H * 0.5f), style = Stroke(2.dp.toPx()))
        drawOval(Color(0xFF795548), Offset((rL + rR) / 2 - 14.dp.toPx(), aY - 12.dp.toPx()), Size(28.dp.toPx(), nY - aY + 24.dp.toPx()), style = Stroke(5.dp.toPx()))
        drawLine(Palette.Active, Offset(rL, aY), Offset(rR, aY), sw)
        drawLine(Palette.Neutral, Offset(rL, nY), Offset(rR, nY), sw)
        label(if (tripped) "RCD TRIPPED" else "RCD", (rL + rR) / 2, H * 0.68f, if (tripped) Palette.Bad else ink, 12f)
        // current dots
        if (!tripped) {
            dotsAlong(listOf(Offset(0f, aY), Offset(W * 0.88f, aY), Offset(W * 0.88f, nY), Offset(0f, nY)), phase, 18, Palette.Current, 3.5.dp.toPx())
        }
        // ground
        val gY = H * 0.95f
        drawLine(Color(0xFF8D6E63), Offset(0f, gY), Offset(W, gY), 2.dp.toPx())
        when (scen) {
            1 -> {
                val px = W * 0.66f
                stickPerson(Offset(px, gY), 70.dp.toPx(), if (tripped) ink else Palette.Bad)
                drawLine(if (tripped) grey else Palette.Active, Offset(px + 20.dp.toPx(), gY - 49.dp.toPx()), Offset(px + 20.dp.toPx(), aY), 2.dp.toPx())
                if (!tripped) dotsAlong(listOf(Offset(px + 20.dp.toPx(), aY), Offset(px + 20.dp.toPx(), gY - 49.dp.toPx()), Offset(px, gY)), phase * 2f, 4, Palette.Bad, 3.dp.toPx())
                label("leak to earth", px - 24.dp.toPx(), gY - 4.dp.toPx(), Palette.Bad, 10f, Paint.Align.RIGHT)
            }
            2 -> {
                val px = W * 0.66f
                stickPerson(Offset(px, gY), 70.dp.toPx(), Palette.Bad)
                drawLine(Palette.Active, Offset(px + 20.dp.toPx(), gY - 49.dp.toPx()), Offset(px + 20.dp.toPx(), aY), 2.dp.toPx())
                drawLine(Palette.Neutral, Offset(px - 17.dp.toPx(), gY - 35.dp.toPx()), Offset(px - 17.dp.toPx(), nY), 2.dp.toPx())
                label("hand on A, hand on N", px, gY - 76.dp.toPx(), Palette.Bad, 10f)
            }
            else -> {}
        }
    }
    val aAmps = 10.0 + leakMa / 1000.0
    Text(
        "Active: ${"%.3f".format(aAmps)} A   Neutral: 10.000 A   Difference: $leakMa mA",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
    Explain(
        when (scen) {
            0 -> "An RCD constantly compares the current going OUT on the active with the current coming BACK on the neutral. Normally they're equal, so it stays on."
            1 -> if (tripped) "Some current leaked through the person to earth, so less came back on the neutral. The RCD saw the 40 mA difference and tripped in a fraction of a second. Shock, but probably survivable." else "Current is leaking through the person to earth…"
            else -> "DANGER: the current goes in through one hand and out the other, straight back on the neutral. Out = back, so the RCD sees NO difference and does NOT trip. An RCD is a backup, never a replacement for isolating and testing."
        },
        if (scen == 2) Palette.Bad else Color.Unspecified
    )
}

@Composable
internal fun BreakerSim() {
    var load by remember { mutableFloatStateOf(10f) }
    var heat by remember { mutableFloatStateOf(0f) }
    var tripped by remember { mutableStateOf(false) }
    var how by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            delay(100)
            if (!tripped) {
                heat = if (load > 20f) (heat + (load - 20f) / 20f * 0.03f).coerceAtMost(1f) else (heat - 0.02f).coerceAtLeast(0f)
                if (heat >= 1f) {
                    tripped = true
                    how = "thermal"
                }
            } else {
                heat = (heat - 0.01f).coerceAtLeast(0f)
            }
        }
    }
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(150.dp)) {
        val bx = size.width * 0.1f
        val bw = size.width * 0.3f
        drawRect(Color(0xFFECEFF1), Offset(bx, 10.dp.toPx()), Size(bw, size.height - 20.dp.toPx()))
        drawRect(ink, Offset(bx, 10.dp.toPx()), Size(bw, size.height - 20.dp.toPx()), style = Stroke(2.dp.toPx()))
        val tY = if (tripped) size.height * 0.62f else size.height * 0.3f
        drawRect(if (tripped) Palette.Bad else Palette.Good, Offset(bx + bw * 0.3f, tY), Size(bw * 0.4f, size.height * 0.16f))
        label("20 A", bx + bw / 2, size.height - 20.dp.toPx(), Color.Black, 12f)
        label(if (tripped) "OFF" else "ON", bx + bw / 2, 26.dp.toPx(), Color.Black, 11f)
        // heat bar
        val hx = size.width * 0.5f
        val hw = size.width * 0.44f
        label("Bimetal strip heat (overload)", hx, 22.dp.toPx(), ink, 11f, Paint.Align.LEFT)
        drawRect(ink.copy(alpha = 0.2f), Offset(hx, 30.dp.toPx()), Size(hw, 16.dp.toPx()))
        drawRect(Color(0xFFFF7043), Offset(hx, 30.dp.toPx()), Size(hw * heat, 16.dp.toPx()))
        label("Load: ${load.roundToInt()} A", hx, 78.dp.toPx(), ink, 13f, Paint.Align.LEFT)
        if (tripped) label(if (how == "magnetic") "MAGNETIC trip: instant!" else "THERMAL trip: overload", hx, 104.dp.toPx(), Palette.Bad, 12f, Paint.Align.LEFT)
    }
    LabeledSlider("Current drawn by the circuit", load, 0f..40f) { load = it }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { tripped = true; how = "magnetic" }) { Text("Short circuit! ⚡") }
        Spacer(Modifier.width(8.dp))
        OutlinedButton(onClick = { if (heat < 0.5f) { tripped = false; how = "" } }) { Text("Reset breaker") }
    }
    Explain(
        when {
            tripped && how == "magnetic" -> "A short circuit makes a massive current. The magnetic coil inside snaps the breaker off in a few thousandths of a second."
            tripped -> "Too much current for too long heated the bimetal strip until it bent and tripped the breaker. That stops the cable overheating and starting a fire. Wait for it to cool before resetting."
            load > 20f -> "Overloaded! Over 20 A, the strip heats up. A small overload takes longer to trip than a big one."
            else -> "Under 20 A the breaker is happy. Slide the load above 20 A, or hit 'Short circuit'."
        }
    )
}
