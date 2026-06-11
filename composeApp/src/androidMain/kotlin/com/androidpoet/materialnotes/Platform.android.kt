package com.androidpoet.materialnotes.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.androidpoet.materialnotes.db.NotesDatabase

fun androidDatabaseDriver(context: Context): SqlDriver =
    AndroidSqliteDriver(
        schema = NotesDatabase.Schema,
        context = context.applicationContext,
        name = "notes.db",
    )
