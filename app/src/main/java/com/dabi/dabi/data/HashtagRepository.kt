package com.dabi.dabi.data

import com.dabi.dabi.api.HashtagService
import javax.inject.Inject

class HashtagRepository @Inject constructor(private val datasource: HashtagRemoteDataSource) {
    suspend fun getAll(): List<String> {
        val hashtags = datasource.getAllHashtags()
        return hashtags.map {
            it.keyword
        }
    }
}