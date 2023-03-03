package com.dabi.dabi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dabi.dabi.data.FeedDao
import com.dabi.dabi.data.FeedRemoteKeyDao
import com.dabi.dabi.data.DateStringConverter
import com.dabi.dabi.data.Feed
import com.dabi.dabi.data.FeedRemoteKey
import com.dabi.dabi.data.StringListConverter

@Database(entities = [Feed::class, FeedRemoteKey::class], version = 2)
@TypeConverters(StringListConverter::class, DateStringConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    abstract fun feedRemoteKeyDao(): FeedRemoteKeyDao
}