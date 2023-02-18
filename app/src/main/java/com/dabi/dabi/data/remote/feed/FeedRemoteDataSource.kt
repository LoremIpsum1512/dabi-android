package com.dabi.dabi.data.remote.feed

import com.dabi.dabi.model.Feed
import com.dabi.dabi.model.PagingResponse
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(private val feedService: FeedService) {
    suspend fun getPaging(offset: Int, style: String?): PagingResponse<Feed> {
        return feedService.getPagingFeed(offset, style = style)
    }
}