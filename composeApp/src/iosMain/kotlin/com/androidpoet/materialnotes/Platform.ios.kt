package com.androidpoet.materialnotes.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.androidpoet.materialnotes.db.NotesDatabase

fun iosDatabaseDriver(): SqlDriver =
    NativeSqliteDriver(
        schema = NotesDatabase.Schema,
        name = "notes.db",
    )
