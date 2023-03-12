package com.dabi.dabi.data

import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

sealed class HandledException {
    class Network(val exception: HttpException) : HandledException()

    class Unknown(val throwable: Throwable) : HandledException()

    class Connection(val exception: UnknownHostException) : HandledException()

    val meaning: MeaningfulException
        get() = when (this) {
            is Network -> {
                Timber.v("{Network} {code: ${exception.code()}} {${exception.response()}}")
                MeaningfulException(
                    title = "Hệ thống đã xảy ra lỗi!",
                    description = "Mã lỗi ${exception.code()}"
                )
            }
            is Connection -> MeaningfulException(
                title = "Đường truyền không ổn định",
                description = "Bạn kiểm tra lại tín hiệu mạng và thử lại lần nữa nhé ❤️"
            )
            is Unknown -> {
                Timber.e(throwable)
                MeaningfulException(
                    title = "U là trời!",
                    description = "Đã có lỗi kỳ cục xảy ra."
                )
            }
        }

    companion object {
        fun of(throwable: Throwable): HandledException {
            return when (throwable) {
                is HttpException -> Network(throwable)
                is UnknownHostException -> Connection(throwable)
                else -> Unknown(throwable)
            }
        }
    }
}

class MeaningfulException(val title: String, val description: String? = null)
