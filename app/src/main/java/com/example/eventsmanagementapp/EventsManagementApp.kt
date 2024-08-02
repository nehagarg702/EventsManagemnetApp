package com.example.eventsmanagementapp

import android.app.Application
import com.example.eventsmanagementapp.data.local.model.Participant
import com.example.eventsmanagementapp.di.appModule
import com.example.eventsmanagementapp.util.DummyDataLoader
import com.example.eventsmanagementapp.util.PreferenceHelper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class EventsManagementApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@EventsManagementApp)
            modules(appModule)
        }

        // Load dummy data if it's the first run
        val dummyDataLoader: DummyDataLoader = get()
        dummyDataLoader.loadDummyDataIfFirstRun()
    }
}
