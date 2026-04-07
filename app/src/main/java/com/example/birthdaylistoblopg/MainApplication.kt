package com.example.birthdaylistoblopg

import android.app.Application
import com.example.birthdaylistoblopg.dependencyinjection.appModules
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    }
}