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



package com.androidpoet.materialnotes.ui.addnote

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.model.Note
import com.androidpoet.materialnotes.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
  private val mainRepository: MainRepository
) : ViewModel() {

  private val dateFormat = SimpleDateFormat("dd-MMM-yy hh:mm aa")
  private val formattedDate: String = dateFormat.format(Date()).toString()

  public fun addNote(note: Note) {
    viewModelScope.launch {
      val random = Random()
      val color =
        Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
      note.backround = color
      note.date = formattedDate
      mainRepository.addNote(note)
    }
  }

  public fun delete(note: Note) {
    viewModelScope.launch {
      mainRepository.deleteNote(note)
    }
  }

  public fun update(note: Note) {
    viewModelScope.launch {
      mainRepository.updateNote(note)
    }
  }
}
