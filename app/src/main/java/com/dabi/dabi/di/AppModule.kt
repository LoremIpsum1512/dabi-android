package com.dabi.dabi.di

import com.dabi.dabi.data.remote.feed.FeedService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun feedService(retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }
}