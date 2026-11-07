package com.sparkstart.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.data.Ask
import com.sparkstart.app.data.Block
import com.sparkstart.app.data.Bullets
import com.sparkstart.app.data.Deep
import com.sparkstart.app.data.FaultFind
import com.sparkstart.app.data.Hazard
import com.sparkstart.app.data.Order
import com.sparkstart.app.data.P
import com.sparkstart.app.data.Quiz
import com.sparkstart.app.data.Std
import com.sparkstart.app.data.Tip
import com.sparkstart.app.data.Vis
import com.sparkstart.app.data.WhyNot
import com.sparkstart.app.visuals.VisualHost

@Composable
fun Header(title: String, onBack: (() -> Unit)? = null, trailing: @Composable () -> Unit = {}) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            TextButton(onClick = onBack) { Text("←", fontSize = 22.sp) }
        } else {
            Spacer(Modifier.width(12.dp))
        }
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        trailing()
    }
}

@Composable
fun Pill(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bg = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val fg = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    Text(
        text,
        color = fg,
        fontSize = 13.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(50))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    )
}

@Composable
fun Callout(icon: String, title: String?, body: String, tint: Color) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(tint.copy(alpha = 0.14f))
            .padding(12.dp)
    ) {
        if (title != null) {
            Text("$icon  $title", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(Modifier.width(4.dp))
            Text(body, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
        } else {
            Row {
                Text(icon, fontSize = 18.sp)
                Spacer(Modifier.width(8.dp))
                Text(body, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun BlockView(b: Block, onSolved: () -> Unit) {
    when (b) {
        is P -> Text(b.text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 6.dp))
        is Bullets -> Column(Modifier.padding(vertical = 4.dp)) {
            b.items.forEach {
                Row(Modifier.padding(vertical = 3.dp)) {
                    Text("•", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        is Tip -> Callout("💡", null, b.text, Color(0xFFFFC107))
        is Hazard -> Callout("⚠️", "Hazard", b.text, Palette.Bad)
        is WhyNot -> Callout("🚫", "Why you can't do it that way", b.text, Color(0xFF8E24AA))
        is FaultFind -> FaultBlock(b)
        is Std -> Callout(
            "📘", "Wiring Rules / Standard: ${b.ref}",
            b.text + "\n\nCheck this in your own copy. Clause numbers and limits can change with amendments.",
            Color(0xFF1E88E5)
        )
        is Ask -> Callout("🧑‍🔧", "Confirm on site", b.text, Color(0xFF00897B))
        is Vis -> VisualHost(b.visual, b.caption)
        is Deep -> DeepBox(b.text)
        is Quiz -> QuizBlock(b, onSolved)
        is Order -> OrderBlock(b, onSolved)
    }
}

@Composable
private fun FaultBlock(b: FaultFind) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF6D4C41).copy(alpha = 0.15f))
            .padding(12.dp)
    ) {
        Text("🔧  How to find the fault", fontWeight = FontWeight.Bold)
        b.steps.forEachIndexed { i, s ->
            Row(Modifier.padding(top = 6.dp)) {
                Text("${i + 1}.", fontWeight = FontWeight.Bold, modifier = Modifier.width(24.dp))
                Text(s, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun DeepBox(text: String) {
    var open by remember(text) { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { open = !open }
            .padding(12.dp)
    ) {
        Text(
            if (open) "🔍  Deeper explanation  ▲" else "🔍  Explain it in more depth  ▼",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        AnimatedVisibility(open) {
            Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun QuizBlock(q: Quiz, onSolved: () -> Unit) {
    var picked by remember(q) { mutableStateOf<Int?>(null) }
    var correct by remember(q) { mutableStateOf(false) }
    Card(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text("❓ Quick check", style = MaterialTheme.typography.labelLarge)
            Text(q.q, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 6.dp))
            q.options.forEachIndexed { i, o ->
                val bg = when {
                    correct && i == q.answer -> Palette.Good.copy(alpha = 0.35f)
                    picked == i && i != q.answer -> Palette.Bad.copy(alpha = 0.30f)
                    else -> MaterialTheme.colorScheme.surface
                }
                Text(
                    o,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bg)
                        .clickable(enabled = !correct) {
                            picked = i
                            if (i == q.answer) {
                                correct = true
                                onSolved()
                            }
                        }
                        .padding(14.dp)
                )
            }
            if (correct) {
                Text("✅  " + q.why, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 6.dp))
            } else if (picked != null) {
                Text("Not quite. Have another go!", modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
fun OrderBlock(o: Order, onSolved: () -> Unit) {
    val shuffled = remember(o) {
        var s = o.steps.shuffled()
        var guard = 0
        while (s == o.steps && o.steps.size > 1 && guard < 10) {
            s = o.steps.shuffled(); guard++
        }
        s
    }
    var chosen by remember(o) { mutableStateOf(listOf<String>()) }
    var wrong by remember(o) { mutableStateOf<String?>(null) }
    val finished = chosen.size == o.steps.size
    Card(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text("🔢 Put it in order", style = MaterialTheme.typography.labelLarge)
            Text(o.prompt, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 6.dp))
            chosen.forEachIndexed { i, s ->
                Text(
                    "${i + 1}. $s",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Palette.Good.copy(alpha = 0.3f))
                        .padding(10.dp)
                )
            }
            if (!finished) {
                Text("Tap the next step:", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
                shuffled.filter { it !in chosen }.forEach { s ->
                    Text(
                        s,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (wrong == s) Palette.Bad.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface)
                            .clickable {
                                if (s == o.steps[chosen.size]) {
                                    chosen = chosen + s
                                    wrong = null
                                    if (chosen.size == o.steps.size) onSolved()
                                } else {
                                    wrong = s
                                }
                            }
                            .padding(10.dp)
                    )
                }
                if (wrong != null) {
                    Text("Not yet. Something else has to happen first.", modifier = Modifier.padding(top = 6.dp))
                }
            } else {
                Text("✅  Nailed it!", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

@Composable
fun StatBox(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}
