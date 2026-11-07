package com.sparkstart.app.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

data class FlagNote(val time: Long, val where: String, val note: String)

/**
 * Saves everything on the phone only (SharedPreferences).
 * Nothing is ever sent anywhere: the app has no internet permission.
 */
class Store(context: Context) {
    private val p = context.getSharedPreferences("sparkstart", Context.MODE_PRIVATE)

    var done by mutableStateOf(p.getStringSet("done", emptySet())?.toSet() ?: emptySet())
        private set
    var xp by mutableIntStateOf(p.getInt("xp", 0))
        private set
    var streak by mutableIntStateOf(p.getInt("streak", 0))
        private set
    var flags by mutableStateOf(loadFlags())
        private set
    /** Bumped whenever something not held in Compose state changes. */
    var version by mutableIntStateOf(0)
        private set

    private var lastDay: Long = p.getLong("lastDay", -1L)

    private fun today(): Long = LocalDate.now().toEpochDay()

    fun displayStreak(): Int = if (lastDay >= today() - 1) streak else 0

    fun touchStreak() {
        val t = today()
        if (lastDay == t) return
        streak = if (lastDay == t - 1) streak + 1 else 1
        lastDay = t
        p.edit().putInt("streak", streak).putLong("lastDay", t).apply()
    }

    fun addXp(n: Int) {
        xp += n
        p.edit().putInt("xp", xp).apply()
    }

    // ---------- lessons ----------
    fun completeLesson(id: String) {
        val first = id !in done
        done = done + id
        p.edit()
            .putStringSet("done", done)
            .putLong("doneAt_$id", System.currentTimeMillis())
            .putInt("pos_$id", 0)
            .apply()
        addXp(if (first) 50 else 10)
        touchStreak()
        version++
    }

    fun doneAt(id: String): Long = p.getLong("doneAt_$id", 0L)
    fun position(id: String): Int = p.getInt("pos_$id", 0)
    fun setPosition(id: String, page: Int) {
        p.edit().putInt("pos_$id", page).apply()
        version++
    }

    fun lastLesson(): String? = p.getString("last", null)
    fun setLast(id: String) {
        p.edit().putString("last", id).apply()
    }

    // ---------- quizzes ----------
    fun bestQuiz(key: String): Int = p.getInt("best_$key", -1)
    fun quizzesTaken(): Int = p.getInt("quizzes", 0)
    fun recordQuiz(key: String, score: Int, total: Int) {
        if (total <= 0) return
        val pct = score * 100 / total
        val e = p.edit().putInt("quizzes", quizzesTaken() + 1)
        if (pct > bestQuiz(key)) e.putInt("best_$key", pct)
        e.apply()
        addXp(score * 5)
        touchStreak()
        version++
    }

    // ---------- flashcards (simple Leitner boxes) ----------
    private val gaps = intArrayOf(0, 1, 3, 7, 14, 30)
    fun box(id: String): Int = p.getInt("box_$id", 0)
    fun isDue(id: String): Boolean = p.getLong("due_$id", 0L) <= today()
    fun gradeCard(id: String, knew: Boolean) {
        val nb = if (knew) minOf(box(id) + 1, gaps.size - 1) else 0
        p.edit().putInt("box_$id", nb).putLong("due_$id", today() + gaps[nb]).apply()
        if (knew) addXp(2)
        touchStreak()
        version++
    }

    // ---------- flags ("this might be wrong") ----------
    private fun loadFlags(): List<FlagNote> {
        val s = p.getString("flags", "[]") ?: "[]"
        return try {
            val a = JSONArray(s)
            (0 until a.length()).map {
                val o = a.getJSONObject(it)
                FlagNote(o.getLong("t"), o.getString("w"), o.getString("n"))
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveFlags() {
        val a = JSONArray()
        flags.forEach { a.put(JSONObject().put("t", it.time).put("w", it.where).put("n", it.note)) }
        p.edit().putString("flags", a.toString()).apply()
    }

    fun addFlag(where: String, note: String) {
        flags = flags + FlagNote(System.currentTimeMillis(), where, note)
        saveFlags()
    }

    fun removeFlag(f: FlagNote) {
        flags = flags - f
        saveFlags()
    }

    fun resetAll() {
        p.edit().clear().apply()
        done = emptySet()
        xp = 0
        streak = 0
        lastDay = -1L
        flags = emptyList()
        version++
    }
}
