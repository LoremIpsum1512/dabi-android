package com.dabi.dabi.data

import androidx.room.*

@Dao
interface FeedRemoteKeyDao {
    @Query("SELECT * FROM feed_remote_keys WHERE id = :id")
    suspend fun remoteKeyBy(id: Int): FeedRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(feeds: List<FeedRemoteKey>)

    @Query("DELETE FROM feed_remote_keys")
    suspend fun clearRemoteKeys()
}