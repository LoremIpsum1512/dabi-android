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

class FeedRepository @Inject constructor(
    private val feedService: FeedService,
) {

    fun getFeedPagingDataStream(
        styleType: StyleType?
    ): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
        ) {
            FeedPagingSource(feedService, style = styleType)
        }.flow
    }

}

//        val pagingSourceFactory = {
//            feedDao.pagingSource()
//        }
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            remoteMediator = FeedRemoteMediator(
//                database = database,
//                feedService = feedService,
//                feedDao = feedDao,
//                remoteKeyDao = feedRemoteKeyDao
//            ),
//            pagingSourceFactory = pagingSourceFactory
//        ).flow