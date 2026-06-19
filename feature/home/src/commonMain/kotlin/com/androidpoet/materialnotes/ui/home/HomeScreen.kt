package com.androidpoet.materialnotes.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidpoet.materialnotes.data.Note
import com.androidpoet.materialnotes.designsystem.sharedNoteBounds
import com.androidpoet.materialnotes.designsystem.AppIcons
import com.androidpoet.materialnotes.designsystem.NoteInk
import com.androidpoet.materialnotes.designsystem.NoteInkMuted

@Composable
fun HomeScreen(
    viewModel: NotesViewModel,
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(syncState) {
        val message = when (val state = syncState) {
            is SyncUiState.Done -> state.message
            is SyncUiState.Error -> state.message
            else -> null
        }
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearSyncStatus()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("New note", fontWeight = FontWeight.SemiBold) },
                icon = { Icon(AppIcons.Add, contentDescription = null) },
                onClick = onAddNote,
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Header(
                noteCount = notes.size,
                userEmail = userEmail,
                syncing = syncState is SyncUiState.Syncing,
                onSync = viewModel::sync,
                onSignOut = viewModel::signOut,
            )

            if (notes.isEmpty()) {
                EmptyState(modifier = Modifier.fillMaxSize())
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 120.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalItemSpacing = 12.dp,
                ) {
                    items(notes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
                            onClick = { onNoteClick(note) },
                            onDelete = { viewModel.delete(note) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    noteCount: Int,
    userEmail: String?,
    syncing: Boolean,
    onSync: () -> Unit,
    onSignOut: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 16.dp, top = 24.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = userEmail ?: when (noteCount) {
                    0 -> "Capture your ideas"
                    1 -> "1 note"
                    else -> "$noteCount notes"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        FilledTonalIconButton(onClick = onSync, enabled = !syncing) {
            if (syncing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(AppIcons.CloudSync, contentDescription = "Sync notes to cloud")
            }
        }
        Spacer(Modifier.size(8.dp))
        FilledTonalIconButton(onClick = onSignOut, enabled = !syncing) {
            Icon(AppIcons.Logout, contentDescription = "Sign out")
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Color(note.backround),
        modifier = Modifier
            .fillMaxWidth()
            .sharedNoteBounds("note-${note.id}"),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            if (note.title.isNotBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NoteInk,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(8.dp))
            }
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                color = NoteInk.copy(alpha = 0.78f),
                maxLines = 7,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Text(
                    text = note.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = NoteInk.copy(alpha = 0.55f),
                )
                Icon(
                    imageVector = AppIcons.Close,
                    contentDescription = "Delete note",
                    tint = NoteInk.copy(alpha = 0.55f),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(50))
                        .clickable(onClick = onDelete)
                        .padding(2.dp)
                        .size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(bottom = 80.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(96.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = AppIcons.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(44.dp),
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Text(
                text = "No notes yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Tap “New note” to capture your first idea.",
                style = MaterialTheme.typography.bodyMedium,
                color = NoteInkMuted,
            )
        }
    }
}
