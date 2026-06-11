package com.androidpoet.materialnotes.di

import androidx.room.RoomDatabase
import com.androidpoet.materialnotes.data.AppDatabase
import com.androidpoet.materialnotes.data.NotesDao
import com.androidpoet.materialnotes.data.getRoomDatabase
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
 * Compile-time dependency graph (Metro). Each platform supplies the Room database builder and the
 * IO dispatcher through the factory; everything else is wired at compile time.
 */
@DependencyGraph(AppScope::class)
interface AppGraph {

    val notesViewModel: NotesViewModel
    val addNoteViewModel: AddNoteViewModel
    val noteDetailViewModelFactory: NoteDetailViewModel.Factory

    @Provides
    @SingleIn(AppScope::class)
    fun provideDatabase(
        builder: RoomDatabase.Builder<AppDatabase>,
        ioDispatcher: CoroutineContext,
    ): AppDatabase = getRoomDatabase(builder, ioDispatcher)

    @Provides
    @SingleIn(AppScope::class)
    fun provideNotesDao(database: AppDatabase): NotesDao = database.notesDao()

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Provides builder: RoomDatabase.Builder<AppDatabase>,
            @Provides ioDispatcher: CoroutineContext,
        ): AppGraph
    }
}

fun buildAppGraph(
    builder: RoomDatabase.Builder<AppDatabase>,
    ioDispatcher: CoroutineContext,
): AppGraph = createGraphFactory<AppGraph.Factory>().create(builder, ioDispatcher)
