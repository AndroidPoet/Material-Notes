package com.androidpoet.materialnotes.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androidpoet.materialnotes.designsystem.sharedNoteBounds
import com.androidpoet.materialnotes.designsystem.AppIcons
import com.androidpoet.materialnotes.designsystem.NoteInk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: String,
    viewModel: NoteDetailViewModel,
    onBack: () -> Unit,
) {
    val note by viewModel.note.collectAsStateWithLifecycle()

    val background = note?.backround?.let { Color(it) } ?: MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .sharedNoteBounds("note-$noteId")
            .background(background),
    ) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(AppIcons.ArrowBack, contentDescription = "Back", tint = NoteInk)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        note?.let { viewModel.delete(it) }
                        onBack()
                    }) {
                        Icon(AppIcons.Delete, contentDescription = "Delete", tint = NoteInk)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
    ) { innerPadding ->
        note?.let { current ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = current.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = NoteInk,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = current.date,
                    style = MaterialTheme.typography.labelLarge,
                    color = NoteInk.copy(alpha = 0.6f),
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = current.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = NoteInk.copy(alpha = 0.85f),
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
    }
}
