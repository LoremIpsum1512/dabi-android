package com.dabi.dabi.data.remote.feed

import com.dabi.dabi.model.Feed
import com.dabi.dabi.model.PagingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {
    @GET("v1/feedbacks/")
    suspend fun getPagingFeed(
        @Query("offset") offset: Int = 0,
        @Query("is_valid") is_valid: Boolean = true,
        @Query("user_style") style: String?
    ): PagingResponse<Feed>
}
