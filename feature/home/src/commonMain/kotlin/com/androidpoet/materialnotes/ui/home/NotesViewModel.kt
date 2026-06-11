package com.androidpoet.materialnotes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidpoet.materialnotes.data.MainRepository
import com.androidpoet.materialnotes.data.Note
import com.androidpoet.materialnotes.data.sync.NoteSyncService
import com.androidpoet.materialnotes.data.sync.SyncResult
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** UI state for the cloud-sync action shown in the home header. */
sealed interface SyncUiState {
    data object Idle : SyncUiState
    data object Syncing : SyncUiState
    data class Done(val message: String) : SyncUiState
    data class Error(val message: String) : SyncUiState
}

@Inject
class NotesViewModel(
    private val mainRepository: MainRepository,
    private val noteSyncService: NoteSyncService,
) : ViewModel() {

    val notes: StateFlow<List<Note>> = mainRepository.getAllNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    private val _syncState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)
    val syncState: StateFlow<SyncUiState> = _syncState.asStateFlow()

    fun delete(note: Note) {
        viewModelScope.launch {
            mainRepository.deleteNote(note)
        }
    }

    fun sync() {
        if (_syncState.value is SyncUiState.Syncing) return
        viewModelScope.launch {
            _syncState.value = SyncUiState.Syncing
            _syncState.value = when (val result = noteSyncService.sync()) {
                is SyncResult.Success ->
                    SyncUiState.Done("Synced — ${result.pushed} up, ${result.pulled} down")
                is SyncResult.NotConfigured ->
                    SyncUiState.Error("Cloud sync not configured (see SupabaseConfig)")
                is SyncResult.Failure ->
                    SyncUiState.Error(result.message)
            }
        }
    }

    fun clearSyncStatus() {
        _syncState.value = SyncUiState.Idle
    }
}
