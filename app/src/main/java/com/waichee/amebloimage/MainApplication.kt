package com.waichee.amebloimage

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainApplication: Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        if (BuildConfig.DEBUG) {
            applicationScope.launch {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}