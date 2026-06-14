package com.androidpoet.materialnotes

import android.app.Application
import com.androidpoet.materialnotes.di.AppGraph
import com.androidpoet.materialnotes.data.androidDatabaseDriver
import com.androidpoet.materialnotes.di.buildAppGraph
import kotlinx.coroutines.Dispatchers

class MaterialNotesApp : Application() {

    lateinit var appGraph: AppGraph
        private set

    override fun onCreate() {
        super.onCreate()
        appGraph = buildAppGraph(androidDatabaseDriver(this), Dispatchers.IO)
    }
}
