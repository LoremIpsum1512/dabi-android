package com.dabi.dabi.di

import android.content.Context
import com.dabi.dabi.FeedListFragment
import com.dabi.dabi.HomeFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun homeComponent(): HomeComponent.Factory

    fun feedComponent(): FeedDetailComponent.Factory

    fun feedListComponent(): FeedListComponent.Factory
}