package com.sparkstart.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sparkstart.app.content.HitKind
import com.sparkstart.app.content.SearchHit
import com.sparkstart.app.content.SearchIndex
import com.sparkstart.app.data.Store

@Composable
fun SearchScreen(store: Store, onOpen: (String, Int) -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableIntStateOf(0) }
    val filters = listOf(
        "All" to null,
        "Lessons" to setOf(HitKind.LESSON, HitKind.FIX),
        "Fault-finding" to setOf(HitKind.FIX),
        "Quizzes" to setOf(HitKind.QUIZ),
        "Flashcards" to setOf(HitKind.CARD)
    )
    val hits = remember(query, filter) { SearchIndex.search(query, filters[filter].second) }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("🔍 Search", fontSize = 26.sp, fontWeight = FontWeight.Black)
            Text("Search every lesson, fault-finding guide, quiz and flashcard. Try 'e/l fault', 'how to test a power board' or 'pump overload trip'.",
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("How to… / fault / trip / test…") },
                trailingIcon = { if (query.isNotEmpty()) TextButton(onClick = { query = "" }) { Text("✕") } }
            )
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 6.dp)) {
                filters.forEachIndexed { i, (name, _) -> Pill(name, filter == i, { filter = i }) }
            }
        }
        if (query.isBlank()) {
            item {
                Text("Popular searches", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
            }
            items(SearchIndex.suggestions) { s ->
                Text(
                    "🔎  $s",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant).clickable { query = s }.padding(12.dp)
                )
            }
        } else {
            item {
                Text(
                    if (hits.isEmpty()) "Nothing found for \"$query\". Try fewer or simpler words (e.g. 'overload', 'pilot', 'RCD')."
                    else "${hits.size} result${if (hits.size == 1) "" else "s"}",
                    style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(vertical = 6.dp)
                )
            }
            items(hits) { h -> HitCard(h, onOpen) }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

private fun highlight(text: String, matched: Set<String>): AnnotatedString = buildAnnotatedString {
    val re = Regex("[A-Za-z0-9/]+|[^A-Za-z0-9/]+")
    for (m in re.findAll(text)) {
        val w = m.value
        val isWord = w.first().isLetterOrDigit()
        val hit = isWord && SearchIndex.tokens(w).any { t -> t in matched }
        if (hit) {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold, background = Palette.Glow.copy(alpha = 0.45f)))
            append(w)
            pop()
        } else append(w)
    }
}

@Composable
private fun HitCard(h: SearchHit, onOpen: (String, Int) -> Unit) {
    val e = h.entry
    var reveal by remember(e) { mutableStateOf(false) }
    Card(
        Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
            when {
                e.kind == HitKind.LESSON || e.kind == HitKind.FIX -> e.lessonId?.let { onOpen(it, e.page) }
                else -> reveal = !reveal
            }
        }
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(e.kind.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(highlight(e.title, h.matched), style = MaterialTheme.typography.titleSmall)
            Text(e.where, style = MaterialTheme.typography.labelSmall)
            when (e.kind) {
                HitKind.LESSON, HitKind.FIX -> {
                    if (h.snippet.isNotBlank()) Text(highlight(h.snippet, h.matched), style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                    Text("Open →", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                }
                HitKind.QUIZ -> {
                    val q = e.quiz!!
                    q.options.forEachIndexed { i, o ->
                        val right = i == q.answer
                        Text(
                            "${if (reveal && right) "✅" else "•"} $o",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (reveal && right) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    if (reveal) {
                        Text(q.why, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
                        e.lessonId?.let { id ->
                            TextButton(onClick = { onOpen(id, e.page) }) { Text("Open the lesson →") }
                        }
                    } else Text("Tap to show the answer", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                }
                HitKind.CARD -> {
                    if (reveal) Text(e.card!!.back, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                    else Text("Tap to flip", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
