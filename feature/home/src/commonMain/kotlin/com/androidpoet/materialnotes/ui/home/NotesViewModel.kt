package com.androidpoet.materialnotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.data.MainRepository
import com.androidpoet.materialnotes.data.Note
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Inject
class NotesViewModel(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val notes: StateFlow<List<Note>> = mainRepository.getAllNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun delete(note: Note) {
        viewModelScope.launch {
            mainRepository.deleteNote(note)
        }
    }
}
