package com.dabi.dabi.data

import com.dabi.dabi.api.HashtagService
import javax.inject.Inject

class HashtagRemoteDataSource @Inject constructor(private val hashtagService: HashtagService) {
    suspend fun getAllHashtags(): List<Hashtag> {
        return getAll { getHashtags(it) }
    }

    private suspend fun getHashtags(limit: Int): PagingResponse<Hashtag> {
        return hashtagService.getHashtag(limit)
    }

}

suspend fun <T> getAll(fn: suspend (limit: Int) -> PagingResponse<T>): List<T> {
    val response = fn(1)
    if (response.count >= 1) {
        return (fn(response.count)).results
    }
    return response.results
}