package com.dabi.dabi.data

data class PagingResponse<T>(
    val next:String?,
    val count: Int,
    val results: List<T>,
)
