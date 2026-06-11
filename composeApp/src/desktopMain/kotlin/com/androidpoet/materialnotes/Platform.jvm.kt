package com.androidpoet.materialnotes.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidpoet.materialnotes.data.AppDatabase
import java.io.File

fun desktopDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbDir = File(System.getProperty("user.home"), ".materialnotes").apply { mkdirs() }
    val dbFile = File(dbDir, "Notes.db")
    return Room.databaseBuilder<AppDatabase>(name = dbFile.absolutePath)
}
