package com.dabi.dabi.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FeedDao {
    @Query("SELECT * FROM feeds ORDER BY createdAt DESC")
    fun pagingSource(): PagingSource<Int, Feed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feeds: List<Feed>)

    @Query("DELETE FROM feeds")
    suspend fun clearAll()

}