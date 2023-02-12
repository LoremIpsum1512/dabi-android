package com.dabi.dabi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dabi.dabi.di.AppComponent

class MainActivity : AppCompatActivity() {
    lateinit var appComponent: AppComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent = (application as DabiApplication).appComponent
        setContentView(R.layout.activity_main)
    }
}