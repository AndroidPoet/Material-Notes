package com.androidpoet.materialnotes.data

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/** Plain multiplatform domain model. Persistence is handled by SQLDelight (see `Note.sq`). */
data class Note(
    val id: String = newNoteId(),
    val title: String = "",
    val date: String = "",
    val backround: Int = 0,
    val content: String = "",
    // Epoch millis at creation. Used for stable, cross-device newest-first ordering, since
    // the random UUID `id` carries no chronology and `date` is a human string, not sortable.
    val createdAt: Long = 0L,
)

/**
 * Client-generated stable identifier. Assigned once when a note is created on a device and then
 * synced as-is, so the same note keeps the same id on every device (see Supabase local-first
 * guidance). A 122-bit random UUIDv4 — collisions are not a practical concern.
 */
@OptIn(ExperimentalUuidApi::class)
fun newNoteId(): String = Uuid.random().toString()
