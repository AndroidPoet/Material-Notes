@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.androidpoet.materialnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.androidpoet.materialnotes.ui.addnote.AddNoteScreen
import com.androidpoet.materialnotes.ui.detail.NoteDetailScreen
import com.androidpoet.materialnotes.ui.home.HomeScreen
import kotlinx.serialization.Serializable

/** Navigation 3 destination keys. Each screen is reachable by adding its key to the back stack. */
@Serializable
data object HomeRoute : NavKey

@Serializable
data object AddNoteRoute : NavKey

@Serializable
data class DetailRoute(val noteId: Int) : NavKey

@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<NavKey>(HomeRoute) }

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                // SaveableStateHolder preserves each entry's UI state; ViewModelStore scopes a fresh
                // ViewModelStoreOwner per entry so `viewModel()` returns an entry-scoped instance.
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<HomeRoute> {
                        HomeScreen(
                            onAddNote = { backStack.add(AddNoteRoute) },
                            onNoteClick = { backStack.add(DetailRoute(it.id)) },
                        )
                    }
                    entry<AddNoteRoute> {
                        AddNoteScreen(onBack = { backStack.removeLastOrNull() })
                    }
                    entry<DetailRoute> { key ->
                        NoteDetailScreen(
                            noteId = key.noteId,
                            onBack = { backStack.removeLastOrNull() },
                        )
                    }
                },
            )
        }
    }
}
