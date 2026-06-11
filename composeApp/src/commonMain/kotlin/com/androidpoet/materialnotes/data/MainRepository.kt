package com.androidpoet.materialnotes.data

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.Flow

@Inject
@SingleIn(AppScope::class)
class MainRepository(
    private val notesDao: NotesDao,
) {

    val getAllNotes: Flow<List<Note>> = notesDao.getNotesList()

    fun getNote(id: Int): Flow<Note?> = notesDao.getNote(id)

    suspend fun addNote(note: Note) = notesDao.insertNote(note)

    suspend fun deleteNote(note: Note) = notesDao.deleteNote(note)

    suspend fun updateNote(note: Note) = notesDao.updateNote(note)
}
