package com.androidpoet.materialnotes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.androidpoet.materialnotes.di.buildAppGraph
import com.androidpoet.materialnotes.di.desktopDatabaseBuilder
import kotlinx.coroutines.Dispatchers

fun main() {
    val appGraph = buildAppGraph(desktopDatabaseBuilder(), Dispatchers.IO)
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Material Notes",
        ) {
            App(appGraph)
        }
    }
}
