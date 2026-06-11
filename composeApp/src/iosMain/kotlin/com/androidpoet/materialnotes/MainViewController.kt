package com.androidpoet.materialnotes

import androidx.compose.ui.window.ComposeUIViewController
import com.androidpoet.materialnotes.di.buildAppGraph
import com.androidpoet.materialnotes.di.iosDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import platform.UIKit.UIViewController

private val appGraph by lazy { buildAppGraph(iosDatabaseBuilder(), Dispatchers.Default) }

fun MainViewController(): UIViewController = ComposeUIViewController {
    App(appGraph)
}
