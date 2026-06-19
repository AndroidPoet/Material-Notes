@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.androidpoet.materialnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.androidpoet.materialnotes.designsystem.LocalSharedTransitionScope
import com.androidpoet.materialnotes.designsystem.rememberViewModel
import com.androidpoet.materialnotes.di.LocalAppGraph
import com.androidpoet.materialnotes.ui.addnote.AddNoteScreen
import com.androidpoet.materialnotes.ui.auth.AuthScreen
import com.androidpoet.materialnotes.ui.detail.NoteDetailScreen
import com.androidpoet.materialnotes.ui.home.HomeScreen
import kotlinx.serialization.Serializable

/** Navigation 3 destination keys. Each screen is reachable by adding its key to the back stack. */
@Serializable
data object HomeRoute : NavKey

@Serializable
data object AddNoteRoute : NavKey

@Serializable
data class DetailRoute(val noteId: String) : NavKey

@Composable
fun AppNavHost() {
    val graph = LocalAppGraph.current
    val session by graph.sessionStore.session.collectAsState()

    val current = session
    if (current == null) {
        // Signed out: the auth gate. Signing in flips the session and this swaps to the notes app.
        AuthScreen(viewModel = remember { graph.authViewModel })
        return
    }

    // A fresh back stack per signed-in user, so a previous session never leaks its navigation state.
    val backStack = remember(current.userId) { mutableStateListOf<NavKey>(HomeRoute) }

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                // SaveableStateHolder preserves each entry's UI state; ViewModelStore scopes a fresh
                // ViewModelStoreOwner per entry so each `viewModel()` is entry-scoped.
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<HomeRoute> {
                        HomeScreen(
                            viewModel = rememberViewModel { graph.notesViewModel },
                            onAddNote = { backStack.add(AddNoteRoute) },
                            onNoteClick = { backStack.add(DetailRoute(it.id)) },
                        )
                    }
                    entry<AddNoteRoute> {
                        AddNoteScreen(
                            viewModel = rememberViewModel { graph.addNoteViewModel },
                            onBack = { backStack.removeLastOrNull() },
                        )
                    }
                    entry<DetailRoute> { key ->
                        NoteDetailScreen(
                            noteId = key.noteId,
                            viewModel = rememberViewModel(key = "note_${key.noteId}") {
                                graph.noteDetailViewModelFactory.create(key.noteId)
                            },
                            onBack = { backStack.removeLastOrNull() },
                        )
                    }
                },
            )
        }
    }
}
