package com.sparkstart.app.visuals

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sparkstart.app.ui.Palette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private val PhaseWhite = Color(0xFF9E9E9E)

@Composable
internal fun ThreePhaseSim() {
    val phase = rememberFlow(0.15f)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(180.dp)) {
        val mid = size.height / 2
        val amp = size.height * 0.36f
        drawLine(ink.copy(alpha = 0.3f), Offset(0f, mid), Offset(size.width, mid), 1.dp.toPx())
        val colors = listOf(Palette.Active, PhaseWhite, Palette.Neutral)
        for (ph in 0..2) {
            val path = Path()
            for (k in 0..200) {
                val x = k / 200f * size.width
                val t = k / 200.0 * 2.0 + phase
                val y = mid - amp * sin(2 * PI * t - ph * 2 * PI / 3).toFloat()
                if (k == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(path, colors[ph], style = Stroke(3.dp.toPx()))
        }
        label("Red (A)", 6.dp.toPx(), 14.dp.toPx(), Palette.Active, 11f, Paint.Align.LEFT)
        label("White (B)", size.width / 2, 14.dp.toPx(), PhaseWhite, 11f)
        label("Blue (C)", size.width - 6.dp.toPx(), 14.dp.toPx(), Palette.Neutral, 11f, Paint.Align.RIGHT)
    }
    Explain("Three-phase = three AC supplies, each one-third of a cycle (120°) behind the last. Each phase is 230 V to neutral, and 400 V between any two phases. The phases take turns peaking, so a three-phase motor gets a smooth, constant push.")
}

@Composable
internal fun InductionMotorSim() {
    var load by remember { mutableFloatStateOf(0.3f) }
    var reversed by remember { mutableStateOf(false) }
    val slip = 0.02f + 0.08f * load
    val dir = if (reversed) -1f else 1f
    val field = rememberFlow(0.5f)
    val rotor = rememberFlow(0.5f * (1f - slip * 4f))
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(220.dp)) {
        val c = Offset(size.width / 2, size.height / 2)
        val r = size.height * 0.42f
        drawCircle(ink.copy(alpha = 0.1f), r, c)
        drawCircle(ink, r, c, style = Stroke(3.dp.toPx()))
        val coilColors = listOf(Palette.Active, PhaseWhite, Palette.Neutral, Palette.Active, PhaseWhite, Palette.Neutral)
        for (k in 0..5) {
            val a = k * PI / 3
            val p = Offset(c.x + (r * 0.88f * cos(a)).toFloat(), c.y + (r * 0.88f * sin(a)).toFloat())
            drawCircle(coilColors[k], 10.dp.toPx(), p)
        }
        // rotating magnetic field arrow
        rotate(dir * field * 360f, c) {
            drawLine(Color(0xFFFFB300).copy(alpha = 0.7f), c, Offset(c.x + r * 0.7f, c.y), 8.dp.toPx(), cap = StrokeCap.Round)
            label("N", c.x + r * 0.62f, c.y - 8.dp.toPx(), Color(0xFFFFB300), 12f)
        }
        // rotor
        rotate(dir * rotor * 360f, c) {
            drawCircle(Color(0xFF90A4AE), r * 0.45f, c)
            for (k in 0..7) {
                val a = k * PI / 4
                drawLine(
                    Color(0xFF455A64),
                    Offset(c.x + (r * 0.15f * cos(a)).toFloat(), c.y + (r * 0.15f * sin(a)).toFloat()),
                    Offset(c.x + (r * 0.43f * cos(a)).toFloat(), c.y + (r * 0.43f * sin(a)).toFloat()),
                    3.dp.toPx()
                )
            }
        }
    }
    val rpm = (1500 * (1 - slip)).roundToInt()
    Text("4-pole motor on 50 Hz: field spins at 1500 rpm, rotor at about $rpm rpm (slip ${(slip * 100).roundToInt()}%)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    LabeledSlider("Load on the motor", load, 0f..1f) { load = it }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { reversed = !reversed }) { Text("Swap any two phases") }
    }
    Explain("The stator coils, fed by three phases, make a magnetic field that ROTATES (yellow arrow). It drags the rotor around by inducing currents in it. That's why it's called an induction motor. The rotor always runs a little slower than the field (slip). More load = more slip. Swap any two phases and the field, and the motor, spin the other way.")
}

@Composable
internal fun DcMotorSim() {
    var volts by remember { mutableFloatStateOf(12f) }
    var reversed by remember { mutableStateOf(false) }
    val spin = rememberFlow(volts / 24f * 0.8f)
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        val c = Offset(size.width / 2, size.height / 2)
        val mh = size.height * 0.7f
        val mw = size.width * 0.16f
        drawRect(Palette.Active, Offset(size.width * 0.08f, c.y - mh / 2), Size(mw, mh))
        drawRect(Palette.Neutral, Offset(size.width * 0.92f - mw, c.y - mh / 2), Size(mw, mh))
        label("N", size.width * 0.08f + mw / 2, c.y + 6.dp.toPx(), Color.White, 18f)
        label("S", size.width * 0.92f - mw / 2, c.y + 6.dp.toPx(), Color.White, 18f)
        val dir = if (reversed) -1f else 1f
        rotate(dir * spin * 360f, c) {
            drawRect(Color(0xFFB87333), Offset(c.x - size.width * 0.22f, c.y - 6.dp.toPx()), Size(size.width * 0.44f, 12.dp.toPx()))
            drawCircle(Color(0xFFFFB300), 14.dp.toPx(), c)
            drawLine(ink, Offset(c.x, c.y - 14.dp.toPx()), Offset(c.x, c.y + 14.dp.toPx()), 2.dp.toPx())
        }
        label("armature coil + commutator", c.x, size.height - 4.dp.toPx(), ink, 10f)
    }
    LabeledSlider("Voltage: ${volts.roundToInt()} V DC (more volts = faster)", volts, 0f..24f) { volts = it }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(onClick = { reversed = !reversed }) { Text("Reverse the polarity") }
    }
    Explain("Current in the coil (armature) makes it a magnet, so it's pushed around between the N and S magnets. The commutator (split ring) and brushes flip the coil's current every half-turn so it keeps spinning the same way. Reverse the supply polarity and it spins backwards. Brushes wear out, which is a common DC motor fault.")
}

@Composable
internal fun TransformerSim() {
    var np by remember { mutableFloatStateOf(1000f) }
    var ns by remember { mutableFloatStateOf(52f) }
    val vin = 230f
    val vout = vin * ns / np
    val ink = ink()
    Canvas(Modifier.fillMaxWidth().height(170.dp)) {
        val l = size.width * 0.28f
        val r = size.width * 0.72f
        val t = size.height * 0.12f
        val b = size.height * 0.88f
        drawRect(Color(0xFF78909C), Offset(l, t), Size(r - l, b - t), style = Stroke(16.dp.toPx()))
        val pTurns = (np / 100f).roundToInt().coerceIn(1, 12)
        val sTurns = (ns / 100f).roundToInt().coerceIn(1, 12)
        for (k in 0 until pTurns) {
            val y = t + 12.dp.toPx() + k * (b - t - 24.dp.toPx()) / pTurns
            drawOval(Palette.Active, Offset(l - 14.dp.toPx(), y), Size(28.dp.toPx(), 8.dp.toPx()), style = Stroke(3.dp.toPx()))
        }
        for (k in 0 until sTurns) {
            val y = t + 12.dp.toPx() + k * (b - t - 24.dp.toPx()) / sTurns
            drawOval(Palette.Neutral, Offset(r - 14.dp.toPx(), y), Size(28.dp.toPx(), 8.dp.toPx()), style = Stroke(3.dp.toPx()))
        }
        label("IN 230 V", l - 22.dp.toPx(), size.height / 2, Palette.Active, 12f, Paint.Align.RIGHT)
        label("OUT ${f1(vout)} V", r + 22.dp.toPx(), size.height / 2, Palette.Neutral, 12f, Paint.Align.LEFT)
        label("iron core", size.width / 2, size.height / 2, ink, 10f)
    }
    Text("V out = 230 × ${ns.roundToInt()} ÷ ${np.roundToInt()} = ${f1(vout)} V  (${if (vout < vin) "step DOWN" else if (vout > vin) "step UP" else "1:1 isolating"})", fontWeight = FontWeight.Bold)
    LabeledSlider("Primary turns: ${np.roundToInt()}", np, 100f..1200f) { np = it }
    LabeledSlider("Secondary turns: ${ns.roundToInt()}", ns, 10f..1200f) { ns = it }
    Explain("AC in the primary coil makes a changing magnetic field in the iron core, which induces a voltage in the secondary coil. The voltage changes by the turns ratio. Power in ≈ power out, so if the voltage goes DOWN, the current available goes UP. Transformers only work on AC because the field has to keep changing.")
}

@Composable
internal fun StarDeltaSim() {
    var mode by remember { mutableIntStateOf(0) }
    val ink = ink()
    PillRow(listOf("Star (Y) for starting", "Delta (Δ) for running"), mode) { mode = it }
    Canvas(Modifier.fillMaxWidth().height(200.dp)) {
        val c = Offset(size.width / 2, size.height * 0.55f)
        val r = size.height * 0.4f
        val pts = (0..2).map { k ->
            val a = -PI / 2 + k * 2 * PI / 3
            Offset(c.x + (r * cos(a)).toFloat(), c.y + (r * sin(a)).toFloat())
        }
        val cols = listOf(Palette.Active, PhaseWhite, Palette.Neutral)
        if (mode == 0) {
            pts.forEachIndexed { k, p ->
                drawLine(cols[k], c, p, 5.dp.toPx())
                val m = Offset((c.x + p.x) / 2, (c.y + p.y) / 2)
                drawCircle(ink, 9.dp.toPx(), m, style = Stroke(2.dp.toPx()))
            }
            drawCircle(ink, 5.dp.toPx(), c)
            label("star point", c.x + 10.dp.toPx(), c.y + 16.dp.toPx(), ink, 10f, Paint.Align.LEFT)
            label("each winding: 230 V", c.x, size.height - 2.dp.toPx(), ink, 11f)
        } else {
            for (k in 0..2) {
                val a = pts[k]
                val b = pts[(k + 1) % 3]
                drawLine(cols[k], a, b, 5.dp.toPx())
                drawCircle(ink, 9.dp.toPx(), Offset((a.x + b.x) / 2, (a.y + b.y) / 2), style = Stroke(2.dp.toPx()))
            }
            label("each winding: 400 V", c.x, size.height - 2.dp.toPx(), ink, 11f)
        }
        listOf("L1", "L2", "L3").forEachIndexed { k, s -> label(s, pts[k].x + 14.dp.toPx(), pts[k].y, cols[k], 11f, Paint.Align.LEFT) }
    }
    Explain(
        if (mode == 0) "Star: each winding only gets 230 V, so the motor draws about ONE-THIRD of the current (and gives about one-third of the torque) it would starting direct-on-line in delta. Gentler on the supply."
        else "Delta: each winding gets the full 400 V, so full power. A star-delta starter starts in star, then a timer switches it to delta once it's up to speed. The motor must be rated for 400 V in delta for this to work."
    )
}
