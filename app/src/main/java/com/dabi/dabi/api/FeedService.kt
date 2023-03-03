package com.dabi.dabi.api

import com.dabi.dabi.data.Feed
import com.dabi.dabi.data.PagingResponse
import com.dabi.dabi.data.StyleType
import com.dabi.dabi.utils.PAGING_LIMIT
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {
    @GET("v1/feedbacks/")
    suspend fun getPagingFeed(
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = PAGING_LIMIT,
        @Query("is_valid") is_valid: Boolean = true,
        @Query("user_style") style: StyleType? = null
    ): PagingResponse<Feed>
}
