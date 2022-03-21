/*
 * Designed and developed by 2022 AndroidPoet (Ranbir Singh)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.androidpoet.materialnotes.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidpoet.materialnotes.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

  abstract fun notesDao(): NotesDao
}
