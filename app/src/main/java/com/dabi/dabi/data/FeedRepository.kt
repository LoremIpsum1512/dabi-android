package com.dabi.dabi.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dabi.dabi.data.local.AppDatabase
import com.dabi.dabi.api.FeedService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource,
    private val feedService: FeedService,
    private val database: AppDatabase,
    private val feedDao: FeedDao,
    private val feedRemoteKeyDao: FeedRemoteKeyDao
) {


    @OptIn(ExperimentalPagingApi::class)
    fun getFeedPagingDataStream(): Flow<PagingData<Feed>> {
        val pagingSourceFactory = {
            feedDao.pagingSource()
        }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = FeedRemoteMediator(
                database = database,
                feedService = feedService,
                feedDao = feedDao,
                remoteKeyDao = feedRemoteKeyDao
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}