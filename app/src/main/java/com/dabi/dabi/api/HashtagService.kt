package com.dabi.dabi.api

import com.dabi.dabi.data.Hashtag
import com.dabi.dabi.data.PagingResponse
import com.dabi.dabi.utils.PAGING_LIMIT
import retrofit2.http.GET
import retrofit2.http.Query

interface HashtagService {
    @GET("v1/recommendation_hashtags/")
    suspend fun getHashtag(
        @Query("limit") limit: Int? = PAGING_LIMIT,
    ): PagingResponse<Hashtag>
}