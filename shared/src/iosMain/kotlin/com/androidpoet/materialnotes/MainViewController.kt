package com.androidpoet.materialnotes

import androidx.compose.ui.window.ComposeUIViewController
import com.androidpoet.materialnotes.di.buildAppGraph
import com.androidpoet.materialnotes.data.iosDatabaseDriver
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIViewController

private val appGraph by lazy { buildAppGraph(iosDatabaseDriver(), Dispatchers.Default) }

fun MainViewController(): UIViewController = ComposeUIViewController {
    App(appGraph)
}
