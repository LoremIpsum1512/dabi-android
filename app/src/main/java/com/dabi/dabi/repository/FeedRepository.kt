package com.dabi.dabi.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dabi.dabi.data.remote.feed.FeedRemoteDataSource
import com.dabi.dabi.model.Feed
import com.dabi.dabi.ui.feed.FeedPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource
) {
    fun getFeedPagingDataStream(style: String?): Flow<PagingData<Feed>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            FeedPagingSource(feedRemoteDataSource, style)
        }.flow
    }
}