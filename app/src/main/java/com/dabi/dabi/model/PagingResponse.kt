package com.dabi.dabi.model

data class PagingResponse<T>(
    val next:String?,
    val count: Int,
    val results: List<T>,
)
