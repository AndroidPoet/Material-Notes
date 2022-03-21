/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidpoet.materialnotes.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.androidpoet.materialnotes.model.Note
import com.androidpoet.materialnotes.persistence.NotesDao
import javax.inject.Inject

class MainRepository @Inject constructor(
  private val notesDao: NotesDao
) {

  val getAllNotes = Pager(
    config = PagingConfig(pageSize = 10),
    pagingSourceFactory = { notesDao.getNotesList() }
  ).flow

  suspend fun addNote(note: Note) {
    notesDao.insertNote(note)
  }

  suspend fun deleteNote(note: Note) {
    notesDao.deleteNote(note)
  }

  suspend fun updateNote(note: Note) {
    notesDao.updateNote(note)
  }
}
