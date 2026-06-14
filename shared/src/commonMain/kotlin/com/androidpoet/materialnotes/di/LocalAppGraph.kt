package com.androidpoet.materialnotes.di

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppGraph = staticCompositionLocalOf<AppGraph> {
    error("AppGraph was not provided. Wrap content in App(appGraph).")
}
