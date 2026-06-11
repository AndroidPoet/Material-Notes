@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.androidpoet.materialnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.androidpoet.materialnotes.ui.addnote.AddNoteScreen
import com.androidpoet.materialnotes.ui.detail.NoteDetailScreen
import com.androidpoet.materialnotes.ui.home.HomeScreen

object Routes {
    const val HOME = "home"
    const val ADD_NOTE = "addnote"
    const val DETAIL = "detail"
    const val ARG_NOTE_ID = "noteId"
    const val DETAIL_ROUTE = "$DETAIL/{$ARG_NOTE_ID}"
    fun detail(noteId: Int) = "$DETAIL/$noteId"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(navController = navController, startDestination = Routes.HOME) {
                composable(Routes.HOME) {
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        HomeScreen(
                            onAddNote = { navController.navigate(Routes.ADD_NOTE) },
                            onNoteClick = { navController.navigate(Routes.detail(it.id)) },
                        )
                    }
                }
                composable(Routes.ADD_NOTE) {
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        AddNoteScreen(onBack = { navController.popBackStack() })
                    }
                }
                composable(
                    route = Routes.DETAIL_ROUTE,
                    arguments = listOf(navArgument(Routes.ARG_NOTE_ID) { type = NavType.IntType }),
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt(Routes.ARG_NOTE_ID) ?: 0
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        NoteDetailScreen(
                            noteId = noteId,
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}
