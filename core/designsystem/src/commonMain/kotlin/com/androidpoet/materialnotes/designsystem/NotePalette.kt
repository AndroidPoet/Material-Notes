package com.androidpoet.materialnotes.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

/** Curated, harmonious pastel colors for note cards — replaces the original random ARGB values. */
val NotePalette: List<Color> = listOf(
    Color(0xFFFFD8A8), // apricot
    Color(0xFFFFC9C9), // blush
    Color(0xFFFFE9A8), // butter
    Color(0xFFD3F9B5), // lime
    Color(0xFFB2F2D7), // mint
    Color(0xFFA5D8FF), // sky
    Color(0xFFD0BFFF), // lavender
    Color(0xFFFCC2E8), // pink
    Color(0xFFE9ECEF), // cloud
)

/** Dark, high-contrast inks used on top of the pastel cards. */
val NoteInk = Color(0xFF2B2B2E)
val NoteInkMuted = Color(0xFF6B6B73)

fun randomNoteColorArgb(): Int = NotePalette[Random.nextInt(NotePalette.size)].toArgb()
