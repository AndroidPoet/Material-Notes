package com.androidpoet.materialnotes.di

import app.cash.sqldelight.db.SqlDriver
import com.androidpoet.materialnotes.db.NotesDatabase
import com.androidpoet.materialnotes.ui.addnote.AddNoteViewModel
import com.androidpoet.materialnotes.ui.detail.NoteDetailViewModel
import com.androidpoet.materialnotes.ui.home.NotesViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory
import kotlin.coroutines.CoroutineContext

/**
 * Compile-time dependency graph (Metro). Each platform supplies the SQLDelight [SqlDriver] and the
 * IO dispatcher through the factory; everything else is wired at compile time.
 */
@DependencyGraph(AppScope::class)
interface AppGraph {

    val notesViewModel: NotesViewModel
    val addNoteViewModel: AddNoteViewModel
    val noteDetailViewModelFactory: NoteDetailViewModel.Factory

    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabase(driver: SqlDriver): NotesDatabase = NotesDatabase(driver)

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides driver: SqlDriver,
            @Provides ioDispatcher: CoroutineContext,
        ): AppGraph
    }
}

fun buildAppGraph(
    driver: SqlDriver,
    ioDispatcher: CoroutineContext,
): AppGraph = createGraphFactory<AppGraph.Factory>().create(driver, ioDispatcher)
