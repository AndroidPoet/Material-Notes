package com.androidpoet.materialnotes.data

/** Plain multiplatform domain model. Persistence is handled by SQLDelight (see `Note.sq`). */
data class Note(
    val id: Int = 0,
    val title: String = "",
    val date: String = "",
    val backround: Int = 0,
    val content: String = "",
)
