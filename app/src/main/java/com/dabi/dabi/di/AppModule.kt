package com.dabi.dabi.di

import android.content.Context
import androidx.room.Room
import com.dabi.dabi.data.local.AppDatabase
import com.dabi.dabi.data.FeedDao
import com.dabi.dabi.data.FeedRemoteKeyDao
import com.dabi.dabi.api.FeedService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(
    includes = [NetworkModule::class, ViewModelModule::class],
    subcomponents = [HomeComponent::class, FeedDetailComponent::class]
)
class AppModule {

    @Singleton
    @Provides
    fun feedService(retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "dabi-database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideFeedDao(db: AppDatabase): FeedDao {
        return db.feedDao()
    }

    @Singleton
    @Provides
    fun provideFeedRemoteKeyDao(db: AppDatabase): FeedRemoteKeyDao {
        return db.feedRemoteKeyDao()
    }
}
