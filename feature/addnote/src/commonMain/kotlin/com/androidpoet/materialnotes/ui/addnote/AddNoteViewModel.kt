package com.androidpoet.materialnotes.ui.addnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.data.MainRepository
import com.androidpoet.materialnotes.data.Note
import com.androidpoet.materialnotes.designsystem.randomNoteColorArgb
import com.androidpoet.materialnotes.designsystem.currentDateString
import com.androidpoet.materialnotes.designsystem.currentEpochMillis
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

@Inject
class AddNoteViewModel(
    private val mainRepository: MainRepository,
) : ViewModel() {

    fun addNote(title: String, content: String, colorArgb: Int = randomNoteColorArgb()) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                backround = colorArgb,
                date = currentDateString(),
                createdAt = currentEpochMillis(),
            )
            mainRepository.addNote(note)
        }
    }
}
