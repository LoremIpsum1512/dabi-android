package com.dabi.dabi.data

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()

    data class Error(val exception: HandledException) : Result<Any>()
}