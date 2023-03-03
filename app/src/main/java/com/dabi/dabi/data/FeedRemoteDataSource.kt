package com.dabi.dabi.data

import com.dabi.dabi.api.FeedService
import javax.inject.Inject

class FeedRemoteDataSource @Inject constructor(private val feedService: FeedService) {
    suspend fun getPaging(
        offset: Int,
        limit: Int?,
    ): PagingResponse<Feed> {
        return feedService.getPagingFeed(offset = offset, limit = limit)
    }
}