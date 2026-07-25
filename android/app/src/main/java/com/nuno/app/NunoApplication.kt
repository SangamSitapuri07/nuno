package com.nuno.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NunoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}