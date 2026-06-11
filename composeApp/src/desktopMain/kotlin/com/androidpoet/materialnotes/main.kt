package com.androidpoet.materialnotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.androidpoet.materialnotes.di.buildAppGraph
import com.androidpoet.materialnotes.data.desktopDatabaseDriver
import kotlinx.coroutines.Dispatchers

fun main() {
    val appGraph = buildAppGraph(desktopDatabaseDriver(), Dispatchers.IO)
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Material Notes",
        ) {
            App(appGraph)
        }
    }
}
