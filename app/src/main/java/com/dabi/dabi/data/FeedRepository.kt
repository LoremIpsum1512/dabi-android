package com.dabi.dabi.data

import androidx.paging.*
import com.dabi.dabi.adapters.FeedUIModel
import com.dabi.dabi.data.local.AppDatabase
import com.dabi.dabi.api.FeedService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class FeedRepository @Inject constructor(
    private val feedService: FeedService,
) {

    fun getFeedPagingDataStream(
        query: FeedQuery?
    ): Flow<PagingData<Feed>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
        ) {
            FeedPagingSource(feedService, query)
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