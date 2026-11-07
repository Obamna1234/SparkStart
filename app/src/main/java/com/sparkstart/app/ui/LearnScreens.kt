package com.sparkstart.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.content.Course
import com.sparkstart.app.data.Group
import com.sparkstart.app.data.Level
import com.sparkstart.app.data.Module
import com.sparkstart.app.data.Order
import com.sparkstart.app.data.Quiz
import com.sparkstart.app.data.Store

@Composable
fun HomeScreen(store: Store, onModule: (String) -> Unit, onLesson: (String) -> Unit, onAbout: () -> Unit) {
    val v = store.version
    val done = store.done
    val core = Course.modules.filter { it.group == Group.CORE }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚡ SparkStart", fontSize = 28.sp, fontWeight = FontWeight.Black, modifier = Modifier.weight(1f))
                TextButton(onClick = onAbout) { Text("ℹ️ About") }
            }
            Text(
                "Electrical basics for apprentices, explained simply. Based on Australian practice and AS/NZS 3000.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth()) {
                StatBox("🔥 ${store.displayStreak()}", "day streak", Modifier.weight(1f))
                StatBox("⭐ ${store.xp}", "XP", Modifier.weight(1f))
                StatBox("✅ ${done.size}/${Course.totalLessons}", "lessons", Modifier.weight(1f))
            }
            val last = store.lastLesson()?.let { Course.lesson(it) }
            if (last != null && v >= 0) {
                Card(
                    Modifier.fillMaxWidth().padding(top = 12.dp).clickable { onLesson(last.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(last.emoji, fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Continue where you left off", style = MaterialTheme.typography.labelLarge)
                            Text(last.title, fontWeight = FontWeight.Bold)
                        }
                        Text("▶", fontSize = 22.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Core skills", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Start at the top and work down. Each lesson is about 5–10 minutes.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
        }
        items(core, key = { it.id }) { m -> ModuleCard(m, done, onModule) }
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                "🏗️ Domestic, commercial and underground mining lessons are in the Industries tab.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun IndustriesScreen(store: Store, onModule: (String) -> Unit) {
    val done = store.done
    val inds = Course.modules.filter { it.group == Group.INDUSTRY }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp)) {
        item {
            Text("🏗️ Industries", fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text(
                "Pick where you work (or want to). Each area has Beginner 🟢, Intermediate 🟡 and Advanced 🔴 lessons on real equipment, the hazards, why things are done a certain way, and how to find faults.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
        }
        items(inds, key = { it.id }) { m -> ModuleCard(m, done, onModule) }
        item {
            Text(
                "Every site has its own procedures. When the app says \"Confirm on site\", your site's rules and your supervisor come first.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}

@Composable
fun ModuleCard(m: Module, done: Set<String>, onModule: (String) -> Unit) {
    val n = m.lessons.count { it.id in done }
    val color = Color(m.color)
    Card(
        Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onModule(m.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(color.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) { Text(m.emoji, fontSize = 26.sp) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(m.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(m.blurb, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { if (m.lessons.isEmpty()) 0f else n.toFloat() / m.lessons.size },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = color
                )
                Text("$n of ${m.lessons.size} done", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun ModuleScreen(store: Store, id: String, onBack: () -> Unit, onLesson: (String) -> Unit, onQuiz: () -> Unit) {
    val m = Course.module(id) ?: return
    val v = store.version
    val done = store.done
    var level by remember(id) { mutableStateOf<Level?>(null) }
    val levelsHere = Level.entries.filter { l -> m.lessons.any { it.level == l } }
    val shown = m.lessons.filter { level == null || it.level == level }
    Column(Modifier.fillMaxSize()) {
        Header("${m.emoji} ${m.title}", onBack)
        LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
            item {
                Text(m.blurb, style = MaterialTheme.typography.bodyMedium)
                if (levelsHere.size > 1) {
                    Row(Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 8.dp)) {
                        Pill("All", level == null, { level = null })
                        levelsHere.forEach { l -> Pill("${l.emoji} ${l.label}", level == l, { level = l }) }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
            items(shown, key = { it.id }) { l ->
                val isDone = l.id in done
                val pos = if (v >= 0) store.position(l.id) else 0
                Card(
                    Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable { onLesson(l.id) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDone) Palette.Good.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(l.emoji, fontSize = 26.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(l.title, fontWeight = FontWeight.Bold)
                            val extra = when {
                                isDone -> "✅ Done – tap to review"
                                pos > 0 -> "▶ Resume at page ${pos + 1}"
                                else -> ""
                            }
                            Text(
                                "${l.level.emoji} ${l.level.label} · ${l.minutes} min · ${l.pages.size} pages  $extra",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
            item {
                Button(onClick = onQuiz, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    Text("🎯 Quiz me on this section")
                }
            }
        }
    }
}

@Composable
fun LessonScreen(store: Store, id: String, onClose: () -> Unit, onOpenLesson: (String) -> Unit) {
    val lesson = Course.lesson(id) ?: return
    val reviewing = id in store.done
    var pageIdx by remember(id) { mutableIntStateOf(store.position(id).coerceIn(0, lesson.pages.lastIndex)) }
    var finished by remember(id) { mutableStateOf(false) }
    var showFlag by remember { mutableStateOf(false) }
    LaunchedEffect(id) { store.setLast(id) }

    if (finished) {
        val next = Course.nextLesson(id)
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎉", fontSize = 64.sp)
            Text("Lesson complete!", fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text(lesson.title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text("Saved to your progress. ⭐ ${store.xp} XP · 🔥 ${store.displayStreak()} day streak")
            Spacer(Modifier.height(24.dp))
            if (next != null) {
                Button(onClick = { onOpenLesson(next.id) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Next: ${next.emoji} ${next.title}")
                }
            }
            OutlinedButton(onClick = onClose, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("Back to the list")
            }
        }
        return
    }

    val page = lesson.pages[pageIdx]
    val needs = page.blocks.count { it is Quiz || it is Order }
    var solved by remember(id, pageIdx) { mutableIntStateOf(0) }
    val canGo = reviewing || solved >= needs

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onClose) { Text("✕", fontSize = 20.sp) }
            LinearProgressIndicator(
                progress = { (pageIdx + 1f) / lesson.pages.size },
                modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp))
            )
            TextButton(onClick = { showFlag = true }) { Text("🚩") }
        }
        key(id, pageIdx) {
            Column(
                Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)
            ) {
                Text(
                    "${lesson.emoji} ${lesson.title} · ${pageIdx + 1}/${lesson.pages.size}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(page.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                page.blocks.forEach { b -> BlockView(b, onSolved = { solved++ }) }
                Spacer(Modifier.height(24.dp))
            }
        }
        Row(Modifier.fillMaxWidth().padding(12.dp)) {
            if (pageIdx > 0) {
                OutlinedButton(onClick = {
                    pageIdx--
                    store.setPosition(id, pageIdx)
                }) { Text("← Back") }
                Spacer(Modifier.width(8.dp))
            }
            val last = pageIdx == lesson.pages.lastIndex
            Button(
                onClick = {
                    if (last) {
                        store.completeLesson(id)
                        finished = true
                    } else {
                        pageIdx++
                        store.setPosition(id, pageIdx)
                    }
                },
                enabled = canGo,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    when {
                        !canGo -> "Answer the question to continue"
                        last -> "Finish lesson ✓"
                        else -> "Next →"
                    }
                )
            }
        }
    }

    if (showFlag) {
        var note by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showFlag = false },
            title = { Text("🚩 Flag this page") },
            text = {
                Column {
                    Text("Think something here is wrong or unclear? Save a note and check it with your TAFE lecturer or supervisor. It's listed under Progress → My flags.")
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("What looks wrong? (optional)") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    store.addFlag("${lesson.title} – page ${pageIdx + 1}: ${page.title}", note)
                    showFlag = false
                }) { Text("Save flag") }
            },
            dismissButton = { TextButton(onClick = { showFlag = false }) { Text("Cancel") } }
        )
    }
}
