package com.androidpoet.materialnotes.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.data.MainRepository
import com.androidpoet.materialnotes.data.Note
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AssistedInject
class NoteDetailViewModel(
    @Assisted private val noteId: String,
    private val mainRepository: MainRepository,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory {
        fun create(noteId: String): NoteDetailViewModel
    }

    val note: StateFlow<Note?> = mainRepository.getNote(noteId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    fun delete(note: Note) {
        viewModelScope.launch {
            mainRepository.deleteNote(note)
        }
    }
}
