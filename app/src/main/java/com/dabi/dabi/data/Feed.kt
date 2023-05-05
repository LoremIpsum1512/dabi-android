package com.dabi.dabi.data

import androidx.room.PrimaryKey
import com.squareup.moshi.*
import java.util.Date

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
//@Entity(tableName = "feeds")
data class Feed(
    @PrimaryKey val pk: Int,
    @Json(name = "thumbnail_image") val thumbnailImage: String,
    val images: List<String>?,
    @Json(name = "post_description") val postDescription: String,
    @Json(name = "media_type") val mediaType: MediaType,
    @Json(name = "created_at") val createdAt: Date,
    val fetchedOrder: Int?,
    val video: String?,
    @Json(name = "pin_position") val pinPositions: List<PinPosition>
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

