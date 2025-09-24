package com.konstandaki.jedyapps

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JahApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Some basic application setup.
    }
}