package com.androidpoet.materialnotes.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidpoet.materialnotes.data.AppDatabase

fun androidDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("Notes.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
