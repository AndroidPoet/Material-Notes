package com.androidpoet.materialnotes.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.androidpoet.materialnotes.db.NotesDatabase
import java.io.File

fun desktopDatabaseDriver(): SqlDriver {
    val dbDir = File(System.getProperty("user.home"), ".materialnotes").apply { mkdirs() }
    val dbFile = File(dbDir, "notes.db")
    val isNew = !dbFile.exists()
    val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
    if (isNew) NotesDatabase.Schema.create(driver)
    return driver
}
