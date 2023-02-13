package com.dabi.dabi.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

enum class MediaType(val value: Int) {
    SingleImage(0),
    Video(1),
    MultiImage(2);

    companion object {
        fun from(value: Int): MediaType {
            return when (value) {
                2 -> MultiImage
                1 -> Video
                else -> SingleImage
            }
        }
    }
}

@JsonClass(generateAdapter = true)
data class Feed(
    val pk: Int,
    @Json(name = "thumbnail_image") val thumbnailImage: String,
    val images: List<String>?,
    @Json(name = "post_description") val postDescription: String,
     @Json(name = "media_type") val mediaType: MediaType,
    val video: String?,
)

class MediaTypeAdapter {
    @ToJson
    fun toJson(enum: MediaType): Int {
        return enum.value
    }

    @FromJson
    fun fromJson(value: Int): MediaType {
        return MediaType.from(value)
    }
}