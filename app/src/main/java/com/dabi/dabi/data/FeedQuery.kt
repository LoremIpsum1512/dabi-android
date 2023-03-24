package com.dabi.dabi.data

import kotlin.math.max

data class FeedQuery(
    val height: HeightQueryValue?,
    val style: StyleType?,
) {
    fun asQueryParams(): FeedQueryParams {
        val (minHeight, maxHeight) = height?.range ?: Pair(null, null)
        return FeedQueryParams(
            style = style,
            minHeight = minHeight,
            maxHeight = maxHeight
        )
    }
}

data class FeedQueryParams(
    val style: StyleType?,
    val minHeight: Int? = null,
    val maxHeight: Int? = null,
)

sealed class HeightQueryValue(open val range: Pair<Int, Int?>, open val name: String) {

    class Below150(
        override val range: Pair<Int, Int?> = Pair(0, 150),
        override val name: String = "~150cm"
    ) : HeightQueryValue(range, name)

    class Below155(
        override val range: Pair<Int, Int?> = Pair(151, 155),
        override val name: String = "151~155cm"
    ) : HeightQueryValue(range, name)


    class below160(
        override val range: Pair<Int, Int?> = Pair(156, 160),
        override val name: String = "151~155cm"
    ) : HeightQueryValue(range, name)

    class below165(
        override val range: Pair<Int, Int?> = Pair(161, 165),
        override val name: String = "151~155cm"
    ) : HeightQueryValue(range, name)

    class below170(
        override val range: Pair<Int, Int?> = Pair(166, 170),
        override val name: String = "151~155cm"
    ) : HeightQueryValue(range, name)

    class over171(
        override val range: Pair<Int, Int?> = Pair(171, null),
        override val name: String = "151~155cm"
    ) : HeightQueryValue(range, name)
}