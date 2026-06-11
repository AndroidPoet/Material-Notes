package com.androidpoet.materialnotes.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// A modern, calm light palette with a confident indigo accent.
private val Indigo = Color(0xFF4C5FD5)
private val IndigoDark = Color(0xFF3A49B0)
private val Canvas = Color(0xFFF6F6FB)
private val Ink = Color(0xFF1B1B1F)

private val LightColors = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E3FF),
    onPrimaryContainer = IndigoDark,
    secondary = Color(0xFF5A5C72),
    background = Canvas,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFEDEDF4),
    onSurfaceVariant = Color(0xFF5F5F6B),
    outlineVariant = Color(0xFFE2E2EC),
)

@Composable
fun MaterialNotesTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content,
    )
}
