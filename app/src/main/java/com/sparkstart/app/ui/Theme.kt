package com.sparkstart.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFE09A00),
    onPrimary = Color(0xFF1A1300),
    primaryContainer = Color(0xFFFFE3A3),
    onPrimaryContainer = Color(0xFF261A00),
    secondary = Color(0xFF1565C0),
    background = Color(0xFFFFFBF2),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF3EEE2),
    onSurfaceVariant = Color(0xFF4A4639)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFC233),
    onPrimary = Color(0xFF1A1300),
    primaryContainer = Color(0xFF4A3800),
    onPrimaryContainer = Color(0xFFFFE3A3),
    secondary = Color(0xFF64B5F6),
    background = Color(0xFF121212),
    surface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0C8B6)
)

@Composable
fun SparkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content
    )
}

/** Fixed colours used in lessons and diagrams. */
object Palette {
    val Active = Color(0xFFE53935)
    val Neutral = Color(0xFF1E88E5)
    val Earth = Color(0xFF43A047)
    val EarthStripe = Color(0xFFFDD835)
    val Current = Color(0xFFFFB300)
    val Good = Color(0xFF2E7D32)
    val Bad = Color(0xFFC62828)
    val Glow = Color(0xFFFFEB3B)
    val Water = Color(0xFF29B6F6)
}
