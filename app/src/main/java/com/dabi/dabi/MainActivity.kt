package com.dabi.dabi

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dabi.dabi.databinding.ActivityMainBinding
import com.dabi.dabi.di.AppComponent
import com.dabi.dabi.di.FeedDetailComponent
import com.dabi.dabi.di.HomeComponent

class MainActivity : AppCompatActivity() {
    lateinit var appComponent: AppComponent
    lateinit var homeComponent: HomeComponent
    lateinit var feedDetailComponent: FeedDetailComponent
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent = (application as DabiApplication).appComponent

        homeComponent = appComponent.homeComponent().create()
        feedDetailComponent = appComponent.feedComponent().create()

        binding =
            DataBindingUtil.setContentView(
                this, R.layout.activity_main
            )

    }
}