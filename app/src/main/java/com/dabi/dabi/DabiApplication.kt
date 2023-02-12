package com.dabi.dabi

import android.app.Application
import com.dabi.dabi.di.AppComponent
import com.dabi.dabi.di.DaggerAppComponent

class DabiApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }


}