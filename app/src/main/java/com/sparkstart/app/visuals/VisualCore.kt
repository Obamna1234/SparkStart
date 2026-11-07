package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.data.Visual
import com.sparkstart.app.ui.Palette
import com.sparkstart.app.ui.Pill
import java.util.Locale
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun VisualHost(v: Visual, caption: String) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text("▶ Interactive. Tap and slide things", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            when (v) {
                Visual.CIRCUIT -> SimpleCircuitSim()
                Visual.WATER -> WaterSim()
                Visual.OHMS -> OhmsSim()
                Visual.SERIES_PARALLEL -> SeriesParallelSim()
                Visual.AC_WAVE -> AcWaveSim()
                Visual.BODY_CURRENT -> BodyCurrentSim()
                Visual.SWITCH_ACTIVE -> SwitchActiveSim()
                Visual.MEN_FAULT -> MenFaultSim()
                Visual.RCD -> RcdSim()
                Visual.BREAKER -> BreakerSim()
                Visual.MULTIMETER -> MultimeterSim()
                Visual.INSULATION -> InsulationSim()
                Visual.CLAMP -> ClampSim()
                Visual.LOOP_IMPEDANCE -> LoopImpedanceSim()
                Visual.VOLTAGE_DROP -> VoltageDropSim()
                Visual.THREE_PHASE -> ThreePhaseSim()
                Visual.INDUCTION_MOTOR -> InductionMotorSim()
                Visual.DC_MOTOR -> DcMotorSim()
                Visual.TRANSFORMER -> TransformerSim()
                Visual.STAR_DELTA -> StarDeltaSim()
                Visual.PUMP_SHARED -> PumpSharedSim()
                Visual.PILOT_CORE -> PilotCoreSim()
                Visual.NER -> NerSim()
                Visual.BACKFEED -> BackfeedSim()
                Visual.HALF_SPLIT -> HalfSplitSim()
            }
            if (caption.isNotBlank()) {
                Text(caption, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

// ------------------------------------------------------------------ helpers

/** A phase value that keeps increasing at [speed] cycles per second (0 = frozen). */
@Composable
internal fun rememberFlow(speed: Float): Float {
    val sp by rememberUpdatedState(speed)
    var phase by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        var last = 0L
        while (true) {
            withFrameNanos { t ->
                if (last != 0L) phase = (phase + (t - last) / 1_000_000_000f * sp) % 1000f
                last = t
            }
        }
    }
    return phase
}

internal fun f1(x: Float): String = String.format(Locale.US, "%.1f", x)
internal fun f2(x: Float): String = String.format(Locale.US, "%.2f", x)

internal fun DrawScope.label(
    text: String, x: Float, y: Float, color: Color,
    sizeSp: Float = 12f, align: Paint.Align = Paint.Align.CENTER, bold: Boolean = true
) {
    val paint = Paint().apply {
        this.color = color.toArgb()
        textSize = sizeSp.sp.toPx()
        isAntiAlias = true
        textAlign = align
        isFakeBoldText = bold
    }
    drawContext.canvas.nativeCanvas.drawText(text, x, y, paint)
}

internal fun pointOnRect(l: Float, t: Float, r: Float, b: Float, f: Float): Offset {
    val w = r - l
    val h = b - t
    val per = 2 * (w + h)
    var d = (((f % 1f) + 1f) % 1f) * per
    if (d < w) return Offset(l + d, t)
    d -= w
    if (d < h) return Offset(r, t + d)
    d -= h
    if (d < w) return Offset(r - d, b)
    d -= w
    return Offset(l, b - d)
}

internal fun pointOnPolyline(pts: List<Offset>, f: Float): Offset {
    if (pts.size < 2) return pts.firstOrNull() ?: Offset.Zero
    val segs = pts.zipWithNext()
    val lens = segs.map { (a, b) -> (b - a).getDistance() }
    val total = lens.sum()
    if (total <= 0f) return pts.first()
    var d = (((f % 1f) + 1f) % 1f) * total
    for (i in segs.indices) {
        if (d <= lens[i]) {
            val (a, b) = segs[i]
            return a + (b - a) * (d / lens[i])
        }
        d -= lens[i]
    }
    return pts.last()
}

internal fun DrawScope.polyline(pts: List<Offset>, color: Color, width: Float, dashed: Boolean = false) {
    val path = Path()
    pts.forEachIndexed { i, p -> if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y) }
    drawPath(
        path, color,
        style = Stroke(
            width = width, cap = StrokeCap.Round,
            pathEffect = if (dashed) PathEffect.dashPathEffect(floatArrayOf(14f, 10f)) else null
        )
    )
}

internal fun DrawScope.dotsAlong(pts: List<Offset>, phase: Float, n: Int, color: Color, radius: Float) {
    for (i in 0 until n) drawCircle(color, radius, pointOnPolyline(pts, phase + i.toFloat() / n))
}

internal fun DrawScope.earthLine(a: Offset, b: Offset, width: Float) {
    drawLine(Palette.Earth, a, b, width)
    drawLine(Palette.EarthStripe, a, b, width * 0.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f)))
}

internal fun DrawScope.earthSymbol(top: Offset, color: Color) {
    val w = 14.dp.toPx()
    val s = 3.dp.toPx()
    drawLine(color, top, top + Offset(0f, 8.dp.toPx()), 2.dp.toPx())
    for (i in 0..2) {
        val y = top.y + 8.dp.toPx() + i * s * 1.6f
        val half = w * (1f - i * 0.3f)
        drawLine(color, Offset(top.x - half, y), Offset(top.x + half, y), 2.dp.toPx())
    }
}

internal fun DrawScope.lamp(c: Offset, r: Float, glow: Float, ink: Color) {
    if (glow > 0.01f) drawCircle(Palette.Glow.copy(alpha = 0.18f + 0.45f * glow), r * (1.3f + glow), c)
    drawCircle(if (glow > 0.01f) Palette.Glow.copy(alpha = 0.4f + 0.6f * glow) else Color(0xFFBDBDBD), r, c)
    drawCircle(ink, r, c, style = Stroke(2.dp.toPx()))
    val k = r * 0.6f
    drawLine(ink, c + Offset(-k, -k), c + Offset(k, k), 2.dp.toPx())
    drawLine(ink, c + Offset(-k, k), c + Offset(k, -k), 2.dp.toPx())
}

internal fun DrawScope.stickPerson(feet: Offset, h: Float, color: Color) {
    val w = 2.5.dp.toPx()
    val head = feet + Offset(0f, -h)
    val hip = feet + Offset(0f, -h * 0.45f)
    drawCircle(color, h * 0.11f, head + Offset(0f, h * 0.11f), style = Stroke(w))
    drawLine(color, head + Offset(0f, h * 0.22f), hip, w)
    drawLine(color, hip, feet + Offset(-h * 0.18f, 0f), w)
    drawLine(color, hip, feet + Offset(h * 0.18f, 0f), w)
    drawLine(color, head + Offset(0f, h * 0.32f), head + Offset(-h * 0.25f, h * 0.5f), w)
    drawLine(color, head + Offset(0f, h * 0.32f), head + Offset(h * 0.3f, h * 0.3f), w)
}

internal fun DrawScope.box(topLeft: Offset, size: Size, fill: Color, stroke: Color) {
    drawRect(fill, topLeft, size)
    drawRect(stroke, topLeft, size, style = Stroke(2.dp.toPx()))
}

@Composable
internal fun PillRow(options: List<String>, selected: Int, onSelect: (Int) -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 4.dp)) {
        options.forEachIndexed { i, o -> Pill(o, selected == i, { onSelect(i) }) }
    }
}

@Composable
internal fun Explain(text: String, color: Color = Color.Unspecified) {
    Text(
        text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = if (color == Color.Unspecified) MaterialTheme.colorScheme.onSurface else color,
        modifier = Modifier.padding(top = 6.dp)
    )
}

@Composable
internal fun LabeledSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onChange: (Float) -> Unit) {
    Text(label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 4.dp))
    Slider(value = value, onValueChange = onChange, valueRange = range)
}

@Composable
internal fun ink(): Color = MaterialTheme.colorScheme.onSurface

// ------------------------------------------------------------------ basic sims

/** Draws a simple loop: battery on the left, lamp on top, switch at the bottom. */
internal fun DrawScope.basicLoop(ink: Color, bg: Color, phase: Float, closed: Boolean, glow: Float, flowing: Boolean) {
    val l = size.width * 0.15f
    val r = size.width * 0.85f
    val t = size.height * 0.22f
    val b = size.height * 0.78f
    val sw = 3.dp.toPx()
    val mx = (l + r) / 2
    val gap = 30.dp.toPx()
    drawLine(ink, Offset(l, t), Offset(r, t), sw)
    drawLine(ink, Offset(r, t), Offset(r, b), sw)
    drawLine(ink, Offset(l, t), Offset(l, b), sw)
    drawLine(ink, Offset(r, b), Offset(mx + gap / 2, b), sw)
    drawLine(ink, Offset(mx - gap / 2, b), Offset(l, b), sw)
    if (closed) drawLine(ink, Offset(mx - gap / 2, b), Offset(mx + gap / 2, b), sw)
    else drawLine(ink, Offset(mx - gap / 2, b), Offset(mx + gap / 2 - 4.dp.toPx(), b - 20.dp.toPx()), sw)
    drawCircle(ink, 4.dp.toPx(), Offset(mx - gap / 2, b))
    drawCircle(ink, 4.dp.toPx(), Offset(mx + gap / 2, b))
    // battery
    val cy = (t + b) / 2
    drawRect(bg, Offset(l - 22.dp.toPx(), cy - 14.dp.toPx()), Size(44.dp.toPx(), 28.dp.toPx()))
    drawLine(ink, Offset(l - 20.dp.toPx(), cy - 7.dp.toPx()), Offset(l + 20.dp.toPx(), cy - 7.dp.toPx()), sw)
    drawLine(ink, Offset(l - 10.dp.toPx(), cy + 7.dp.toPx()), Offset(l + 10.dp.toPx(), cy + 7.dp.toPx()), sw * 2)
    label("+", l + 28.dp.toPx(), cy - 4.dp.toPx(), ink, 14f)
    label("supply", l + 6.dp.toPx(), cy + 34.dp.toPx(), ink, 11f, Paint.Align.LEFT)
    lamp(Offset(mx, t), 17.dp.toPx(), glow, ink)
    label("switch", mx, b + 22.dp.toPx(), ink, 11f)
    if (flowing) {
        for (i in 0 until 14) drawCircle(Palette.Current, 4.5.dp.toPx(), pointOnRect(l, t, r, b, phase + i / 14f))
    }
}

@Composable
internal fun SimpleCircuitSim() {
    var on by remember { mutableStateOf(false) }
    val phase = rememberFlow(if (on) 0.12f else 0f)
    val ink = ink()
    val bg = MaterialTheme.colorScheme.surfaceVariant
    Canvas(Modifier.fillMaxWidth().height(210.dp)) {
        basicLoop(ink, bg, phase, on, if (on) 1f else 0f, on)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { on = !on }) { Text(if (on) "Turn the switch OFF" else "Turn the switch ON") }
    }
    Explain(
        if (on) "The loop is complete, so current (the yellow dots) flows from the supply, through the lamp and back. The lamp lights."
        else "The switch makes a gap in the loop. No complete loop = no current = no light. Electricity ALWAYS needs a way back to where it came from."
    )
}

@Composable
internal fun WaterSim() {
    var pressure by remember { mutableFloatStateOf(0.6f) }
    var narrow by remember { mutableFloatStateOf(0.4f) }
    val flow = pressure / (0.2f + narrow)
    val phase = rememberFlow(flow * 0.25f)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        val tl = size.width * 0.05f
        val tr = size.width * 0.28f
        val tt = size.height * 0.08f
        val tb = size.height * 0.92f
        val level = tb - (tb - tt) * pressure
        drawRect(Palette.Water.copy(alpha = 0.7f), Offset(tl, level), Size(tr - tl, tb - level))
        drawRect(ink, Offset(tl, tt), Size(tr - tl, tb - tt), style = Stroke(2.dp.toPx()))
        label("Pressure", (tl + tr) / 2, tt + 16.dp.toPx(), ink, 11f)
        label("= VOLTAGE", (tl + tr) / 2, tt + 30.dp.toPx(), Palette.Active, 11f)
        val pipeH = 30.dp.toPx() * (1.1f - narrow)
        val py = size.height * 0.75f
        drawRect(Palette.Water.copy(alpha = 0.35f), Offset(tr, py - pipeH / 2), Size(size.width * 0.67f, pipeH))
        drawLine(ink, Offset(tr, py - pipeH / 2), Offset(size.width * 0.95f, py - pipeH / 2), 2.dp.toPx())
        drawLine(ink, Offset(tr, py + pipeH / 2), Offset(size.width * 0.95f, py + pipeH / 2), 2.dp.toPx())
        for (i in 0 until 10) {
            val x = tr + (((phase + i / 10f) % 1f)) * size.width * 0.67f
            drawCircle(Color.White, pipeH * 0.22f, Offset(x, py))
        }
        label("Pipe width = RESISTANCE (narrow = more resistance)", size.width * 0.62f, py - pipeH / 2 - 12.dp.toPx(), ink, 10f)
        label("Flow = CURRENT", size.width * 0.62f, py + pipeH / 2 + 18.dp.toPx(), Palette.Current, 11f)
    }
    LabeledSlider("Water pressure (voltage)", pressure, 0f..1f) { pressure = it }
    LabeledSlider("How narrow the pipe is (resistance)", narrow, 0f..1f) { narrow = it }
    Explain("More pressure → more flow. Narrower pipe → less flow. Electricity works the same way: more volts push more amps; more ohms (resistance) let fewer amps through.")
}

@Composable
internal fun OhmsSim() {
    var v by remember { mutableFloatStateOf(12f) }
    var r by remember { mutableFloatStateOf(6f) }
    val i = if (r > 0f) v / r else 0f
    val phase = rememberFlow(min(i, 6f) * 0.06f)
    val ink = ink()
    val bg = MaterialTheme.colorScheme.surfaceVariant
    Canvas(Modifier.fillMaxWidth().height(200.dp)) {
        basicLoop(ink, bg, phase, true, min(i / 4f, 1f), i > 0.01f)
    }
    Text(
        "I = V ÷ R = ${v.roundToInt()} ÷ ${r.roundToInt()} = ${f2(i)} A",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 4.dp)
    )
    LabeledSlider("Voltage: ${v.roundToInt()} V", v, 0f..24f) { v = it.roundToInt().toFloat() }
    LabeledSlider("Resistance: ${r.roundToInt()} Ω", r, 1f..48f) { r = it.roundToInt().toFloat() }
    Explain("Double the volts → double the amps. Double the ohms → half the amps. That's Ohm's law.")
}

@Composable
internal fun SeriesParallelSim() {
    var parallel by remember { mutableIntStateOf(0) }
    var broken by remember { mutableStateOf(false) }
    val isPar = parallel == 1
    val flowing = isPar || !broken
    val phase = rememberFlow(if (flowing) 0.1f else 0f)
    val ink = ink()
    val bg = MaterialTheme.colorScheme.surfaceVariant
    PillRow(listOf("Series (one path)", "Parallel (many paths)"), parallel) { parallel = it }
    Canvas(Modifier.fillMaxWidth().height(210.dp)) {
        val l = size.width * 0.12f
        val r = size.width * 0.9f
        val t = size.height * 0.18f
        val b = size.height * 0.85f
        val sw = 3.dp.toPx()
        val rad = 14.dp.toPx()
        if (!isPar) {
            drawRect(ink, Offset(l, t), Size(r - l, b - t), style = Stroke(sw))
            val xs = listOf(0.35f, 0.55f, 0.75f).map { l + (r - l) * (it - 0.12f) / 0.78f * 1.0f }
            xs.forEachIndexed { k, x ->
                if (k == 1 && broken) {
                    drawRect(bg, Offset(x - rad, t - rad), Size(rad * 2, rad * 2))
                    label("✕", x, t + 5.dp.toPx(), Palette.Bad, 18f)
                } else {
                    lamp(Offset(x, t), rad, if (broken) 0f else 0.35f, ink)
                }
            }
            if (flowing) for (k in 0 until 14) drawCircle(Palette.Current, 4.dp.toPx(), pointOnRect(l, t, r, b, phase + k / 14f))
        } else {
            drawLine(ink, Offset(l, t), Offset(r, t), sw)
            drawLine(ink, Offset(l, b), Offset(r, b), sw)
            drawLine(ink, Offset(l, t), Offset(l, b), sw)
            val xs = listOf(0.4f, 0.62f, 0.84f).map { size.width * it }
            xs.forEachIndexed { k, x ->
                drawLine(ink, Offset(x, t), Offset(x, b), sw)
                val cy = (t + b) / 2
                if (k == 1 && broken) {
                    drawRect(bg, Offset(x - rad, cy - rad), Size(rad * 2, rad * 2))
                    label("✕", x, cy + 5.dp.toPx(), Palette.Bad, 18f)
                } else {
                    lamp(Offset(x, cy), rad, 1f, ink)
                    for (d in 0 until 4) {
                        val y = t + (((phase + d / 4f) % 1f)) * (b - t)
                        drawCircle(Palette.Current, 4.dp.toPx(), Offset(x, y))
                    }
                }
            }
        }
        label("supply", l - 2.dp.toPx(), (t + b) / 2, ink, 10f, Paint.Align.RIGHT)
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { broken = !broken }) { Text(if (broken) "Put the middle globe back" else "Remove the middle globe") }
    }
    Explain(
        when {
            !isPar && !broken -> "Series: one single path. The voltage is shared between the globes, so they're dim."
            !isPar && broken -> "Series: take one globe out and the ONLY path is broken. They ALL go out. (Old Christmas lights!)"
            isPar && !broken -> "Parallel: each globe has its own path and gets the full voltage, so they're all bright. Houses are wired in parallel."
            else -> "Parallel: one globe out, the others keep working. That's why every light and power point in a house is in parallel."
        }
    )
}

@Composable
internal fun AcWaveSim() {
    var mode by remember { mutableIntStateOf(0) }
    val phase = rememberFlow(0.25f)
    val ink = ink()
    PillRow(listOf("AC (mains)", "DC (battery)"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(170.dp)) {
        val mid = size.height / 2
        val amp = size.height * 0.38f
        drawLine(ink.copy(alpha = 0.4f), Offset(0f, mid), Offset(size.width, mid), 1.dp.toPx())
        label("0 V", 4.dp.toPx(), mid - 4.dp.toPx(), ink, 10f, Paint.Align.LEFT)
        if (mode == 0) {
            val path = Path()
            for (k in 0..200) {
                val x = k / 200f * size.width
                val y = mid - amp * sin(2 * PI * 2 * k / 200.0).toFloat()
                if (k == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, Palette.Active, style = Stroke(3.dp.toPx()))
            val fx = (phase % 1f)
            val dy = mid - amp * sin(2 * PI * 2 * fx).toFloat()
            drawCircle(Palette.Current, 7.dp.toPx(), Offset(fx * size.width, dy))
            label("+325 V peak", size.width - 4.dp.toPx(), mid - amp - 4.dp.toPx() + 14.dp.toPx(), ink, 10f, Paint.Align.RIGHT)
            label("−325 V peak", size.width - 4.dp.toPx(), mid + amp, ink, 10f, Paint.Align.RIGHT)
        } else {
            val y = mid - amp * 0.5f
            drawLine(Palette.Neutral, Offset(0f, y), Offset(size.width, y), 3.dp.toPx())
            val fx = (phase % 1f)
            drawCircle(Palette.Current, 7.dp.toPx(), Offset(fx * size.width, y))
            label("steady, one direction", size.width / 2, y - 10.dp.toPx(), ink, 11f)
        }
    }
    Explain(
        if (mode == 0) "AC (alternating current) swings positive and negative, 50 full cycles every second (50 Hz) in Australia. We call it 230 V because that's its 'effective' (RMS) value. It actually peaks at about 325 V."
        else "DC (direct current) pushes one way only and stays steady, like a battery or a solar panel."
    )
}
