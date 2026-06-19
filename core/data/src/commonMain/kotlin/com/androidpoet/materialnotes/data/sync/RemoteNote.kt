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
    // Packed ARGB colour. The local model keeps it as a signed Int (Compose's Color.toArgb()), but
    // Postgres stores the value unsigned (e.g. 0xFFB8E0F5 → 4290312015), which overflows a signed
    // Int on the way back. Carry it as a Long over the wire; `.toInt()` truncates the low 32 bits to
    // recover the exact signed colour, so the round-trip is lossless either way.
    val backround: Long,
    val content: String,
    @SerialName("created_at") val createdAt: Long,
)

fun Note.toRemote(): RemoteNote = RemoteNote(
    id = id,
    title = title,
    date = date,
    backround = backround.toLong(),
    content = content,
    createdAt = createdAt,
)

fun RemoteNote.toDomain(): Note = Note(
    id = id,
    title = title,
    date = date,
    backround = backround.toInt(),
    content = content,
    createdAt = createdAt,
)
