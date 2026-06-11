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

    fun getNote(id: Int): Flow<Note?> =
        queries.selectById(id.toLong()).asFlow().mapToOneOrNull(ioContext).map { it?.toDomain() }

    suspend fun addNote(note: Note) = withContext(ioContext) {
        queries.insertNote(note.title, note.date, note.backround.toLong(), note.content)
    }

    suspend fun deleteNote(note: Note) = withContext(ioContext) {
        queries.deleteById(note.id.toLong())
    }

    suspend fun updateNote(note: Note) = withContext(ioContext) {
        queries.updateNote(note.title, note.date, note.backround.toLong(), note.content, note.id.toLong())
    }
}

private fun NoteRow.toDomain() = Note(
    id = id.toInt(),
    title = title,
    date = date,
    backround = backround.toInt(),
    content = content,
)
