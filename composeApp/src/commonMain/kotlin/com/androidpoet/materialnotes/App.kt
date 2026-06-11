package com.androidpoet.materialnotes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.androidpoet.materialnotes.di.AppGraph
import com.androidpoet.materialnotes.di.LocalAppGraph
import com.androidpoet.materialnotes.navigation.AppNavHost
import com.androidpoet.materialnotes.designsystem.MaterialNotesTheme

@Composable
fun App(appGraph: AppGraph) {
    CompositionLocalProvider(LocalAppGraph provides appGraph) {
        MaterialNotesTheme {
            AppNavHost()
        }
    }
}
