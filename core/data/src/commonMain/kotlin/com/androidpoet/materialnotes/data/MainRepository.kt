package com.androidpoet.materialnotes.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.androidpoet.materialnotes.db.NotesDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import com.androidpoet.materialnotes.db.Note as NoteRow

@Inject
@SingleIn(AppScope::class)
class MainRepository(
    private val database: NotesDatabase,
    private val ioContext: CoroutineContext,
) {

    private val queries get() = database.noteQueries

    val getAllNotes: Flow<List<Note>> =
        queries.selectAll().asFlow().mapToList(ioContext).map { rows -> rows.map { it.toDomain() } }

    fun getNote(id: String): Flow<Note?> =
        queries.selectById(id).asFlow().mapToOneOrNull(ioContext).map { it?.toDomain() }

    suspend fun addNote(note: Note) = withContext(ioContext) {
        queries.insertNote(note.id, note.title, note.date, note.backround.toLong(), note.content, note.createdAt)
    }

    suspend fun deleteNote(note: Note) = withContext(ioContext) {
        queries.deleteById(note.id)
    }

    suspend fun updateNote(note: Note) = withContext(ioContext) {
        queries.updateNote(note.title, note.date, note.backround.toLong(), note.content, note.id)
    }

    /** One-shot snapshot of every note — used by the Supabase sync to push the local store up. */
    suspend fun getAllNotesOnce(): List<Note> = withContext(ioContext) {
        queries.selectAll().executeAsList().map { it.toDomain() }
    }

    /** Insert-or-replace by id — used when pulling notes down from Supabase. */
    suspend fun upsertNote(note: Note) = withContext(ioContext) {
        queries.upsertNote(note.id, note.title, note.date, note.backround.toLong(), note.content, note.createdAt)
    }
}

private fun NoteRow.toDomain() = Note(
    id = id,
    title = title,
    date = date,
    backround = backround.toInt(),
    content = content,
    createdAt = createdAt,
)
