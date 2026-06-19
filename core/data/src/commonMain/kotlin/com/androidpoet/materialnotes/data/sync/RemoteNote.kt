package com.androidpoet.materialnotes.data.sync

import com.androidpoet.materialnotes.data.Note
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wire model for a note row in Supabase. The column names match the local SQLDelight schema
 * one-to-one so a row round-trips losslessly. See `supabase/schema.sql`.
 */
@Serializable
data class RemoteNote(
    val id: String,
    val title: String,
    val date: String,
    val backround: Int,
    val content: String,
    @SerialName("created_at") val createdAt: Long,
)

fun Note.toRemote(): RemoteNote = RemoteNote(
    id = id,
    title = title,
    date = date,
    backround = backround,
    content = content,
    createdAt = createdAt,
)

fun RemoteNote.toDomain(): Note = Note(
    id = id,
    title = title,
    date = date,
    backround = backround,
    content = content,
    createdAt = createdAt,
)
