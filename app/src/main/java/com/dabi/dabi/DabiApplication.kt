package com.dabi.dabi

import android.app.Application
import com.dabi.dabi.di.AppComponent
import com.dabi.dabi.di.DaggerAppComponent
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


class DabiApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}