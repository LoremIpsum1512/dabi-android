package com.dabi.dabi.data.remote.feed

import com.dabi.dabi.model.Feed
import com.dabi.dabi.model.PagingResponse

class FeedRemoteDataSource {
    suspend fun getPaging(): PagingResponse<Feed> {
        return feedService.getPagingFeed(0)
    }
}