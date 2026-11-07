package com.sparkstart.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sparkstart.app.data.Store

sealed interface Screen {
    data object Home : Screen
    data object Industries : Screen
    data class ModuleS(val id: String) : Screen
    data class LessonS(val id: String) : Screen
    data object PracticeHub : Screen
    data class QuizS(val moduleId: String?) : Screen
    data object Cards : Screen
    data object Calc : Screen
    data object ProgressS : Screen
    data object Flags : Screen
    data object About : Screen
}

private data class Tab(val screen: Screen, val icon: String, val label: String)

@Composable
fun App(store: Store) {
    val stack = remember { mutableStateListOf<Screen>(Screen.Home) }
    val current = stack.last()

    fun go(s: Screen) {
        stack.add(s)
    }

    fun back() {
        if (stack.size > 1) stack.removeAt(stack.lastIndex)
    }

    fun tab(s: Screen) {
        stack.clear()
        stack.add(s)
    }

    BackHandler(enabled = stack.size > 1) { back() }

    val tabs = listOf(
        Tab(Screen.Home, "📚", "Learn"),
        Tab(Screen.Industries, "🏗️", "Industries"),
        Tab(Screen.PracticeHub, "🎯", "Practice"),
        Tab(Screen.ProgressS, "📈", "Progress")
    )
    val showBar = current !is Screen.LessonS

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBar) {
                NavigationBar {
                    tabs.forEach { t ->
                        NavigationBarItem(
                            selected = stack.first() == t.screen,
                            onClick = { tab(t.screen) },
                            icon = { Text(t.icon, fontSize = 20.sp) },
                            label = { Text(t.label) }
                        )
                    }
                }
            }
        }
    ) { pad ->
        Box(Modifier.padding(pad).fillMaxSize()) {
            when (val s = current) {
                Screen.Home -> HomeScreen(
                    store,
                    onModule = { go(Screen.ModuleS(it)) },
                    onLesson = { go(Screen.LessonS(it)) },
                    onAbout = { go(Screen.About) }
                )
                Screen.Industries -> IndustriesScreen(store, onModule = { go(Screen.ModuleS(it)) })
                is Screen.ModuleS -> ModuleScreen(
                    store, s.id,
                    onBack = { back() },
                    onLesson = { go(Screen.LessonS(it)) },
                    onQuiz = { go(Screen.QuizS(s.id)) }
                )
                is Screen.LessonS -> LessonScreen(
                    store, s.id,
                    onClose = { back() },
                    onOpenLesson = { next ->
                        back()
                        go(Screen.LessonS(next))
                    }
                )
                Screen.PracticeHub -> PracticeHubScreen(
                    store,
                    onQuiz = { go(Screen.QuizS(it)) },
                    onCards = { go(Screen.Cards) },
                    onCalc = { go(Screen.Calc) },
                    onFlags = { go(Screen.Flags) }
                )
                is Screen.QuizS -> QuizScreen(store, s.moduleId, onBack = { back() })
                Screen.Cards -> FlashcardScreen(store, onBack = { back() })
                Screen.Calc -> CalcScreen(store, onBack = { back() })
                Screen.ProgressS -> ProgressScreen(
                    store,
                    onLesson = { go(Screen.LessonS(it)) },
                    onFlags = { go(Screen.Flags) },
                    onAbout = { go(Screen.About) }
                )
                Screen.Flags -> FlagsScreen(store, onBack = { back() })
                Screen.About -> AboutScreen(store, onBack = { back() })
            }
        }
    }
}
