package com.androidpoet.materialnotes.ui.addnote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.androidpoet.materialnotes.designsystem.AppIcons
import com.androidpoet.materialnotes.designsystem.NoteInk
import com.androidpoet.materialnotes.designsystem.NoteInkMuted
import com.androidpoet.materialnotes.designsystem.NotePalette
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: AddNoteViewModel,
    onBack: () -> Unit,
) {

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(NotePalette.first()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("New note", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(AppIcons.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    FilledTonalIconButton(
                        onClick = {
                            when {
                                title.isBlank() ->
                                    scope.launch { snackbarHostState.showSnackbar("Please enter a title") }
                                content.isBlank() ->
                                    scope.launch { snackbarHostState.showSnackbar("Please write something") }
                                else -> {
                                    viewModel.addNote(title.trim(), content.trim(), selectedColor.toArgb())
                                    onBack()
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Icon(AppIcons.Check, contentDescription = "Save")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(8.dp))
            PlainField(
                value = title,
                onValueChange = { title = it },
                placeholder = "Title",
                textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(Modifier.height(12.dp))
            PlainField(
                value = content,
                onValueChange = { content = it },
                placeholder = "Start writing…",
                textStyle = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(28.dp))
            Text(
                text = "COLOR",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = NoteInkMuted,
            )
            Spacer(Modifier.height(12.dp))
            ColorPicker(
                selected = selectedColor,
                onSelect = { selectedColor = it },
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PlainField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    textStyle: androidx.compose.ui.text.TextStyle,
) {
    Box {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = textStyle,
                color = NoteInkMuted.copy(alpha = 0.6f),
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = textStyle.copy(color = NoteInk),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ColorPicker(
    selected: Color,
    onSelect: (Color) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        NotePalette.forEach { color ->
            val isSelected = color == selected
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(4.dp)
                    .size(32.dp)
                    .background(color = color, shape = CircleShape)
                    .clickable { onSelect(color) },
            )
        }
    }
}
