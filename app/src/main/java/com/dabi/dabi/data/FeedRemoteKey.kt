package com.dabi.dabi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StyleType(private val value: String) {
    Feminine("feminine"),
    Lovely("lovely"),
    Sexy("sexy"),
    Simple("simple"),
    Street("street"),
    Office("office");

    override fun toString(): String {
        return value
    }

    companion object {
        fun from(value: String): StyleType {
            return when (value) {
                Feminine.value -> Feminine
                Lovely.value -> Lovely
                Sexy.value -> Sexy
                Simple.value -> Simple
                Street.value -> Street
                Office.value -> Office
                else -> Simple
            }
        }
    }
}

@Entity(tableName = "feed_remote_keys")
data class FeedRemoteKey(
    @PrimaryKey()
    val id: Int,
    val nextOffset: Int? = null,
    val prevOffset: Int? = null
)





