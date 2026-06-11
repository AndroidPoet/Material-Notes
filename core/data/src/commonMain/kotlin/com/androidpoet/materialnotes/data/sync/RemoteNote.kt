package com.androidpoet.materialnotes.data.sync

import com.androidpoet.materialnotes.data.Note
import kotlinx.serialization.Serializable

/**
 * Wire model for a note row in Supabase. The column names match the local SQLDelight schema
 * one-to-one so a row round-trips losslessly. See `supabase/schema.sql`.
 */
@Serializable
data class RemoteNote(
    val id: Int,
    val title: String,
    val date: String,
    val backround: Int,
    val content: String,
)

fun Note.toRemote(): RemoteNote = RemoteNote(
    id = id,
    title = title,
    date = date,
    backround = backround,
    content = content,
)

fun RemoteNote.toDomain(): Note = Note(
    id = id,
    title = title,
    date = date,
    backround = backround,
    content = content,
)
