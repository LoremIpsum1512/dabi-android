package com.dabi.dabi.data

import com.dabi.dabi.adapters.FilterEntry

data class FeedQuery(
    val height: HeightQueryValue?,
    val style: StyleType?,
    val weight: WeightQueryValue?,
    val hashtags: List<String>?
) {
    fun asQueryParams(): FeedQueryParams {
        val (minHeight, maxHeight) = height?.range ?: Pair(null, null)
        val (minWeight, maxWeight) = weight?.range ?: Pair(null, null)

        return FeedQueryParams(
            style = style,
            minHeight = minHeight,
            maxHeight = maxHeight,
            minWeight = minWeight,
            maxWeight = maxWeight,
            hashtags = hashtags
        )
    }
}

data class FeedQueryParams(
    val style: StyleType?,
    val minHeight: Int? = null,
    val maxHeight: Int? = null,
    val minWeight: Int? = null,
    val maxWeight: Int? = null,
    val hashtags: List<String>? = null
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


    class Below160(
        override val range: Pair<Int, Int?> = Pair(156, 160),
        override val name: String = "156~160cm"
    ) : HeightQueryValue(range, name)

    class Below165(
        override val range: Pair<Int, Int?> = Pair(161, 165),
        override val name: String = "161~165cm"
    ) : HeightQueryValue(range, name)

    class Below170(
        override val range: Pair<Int, Int?> = Pair(166, 170),
        override val name: String = "166~170cm"
    ) : HeightQueryValue(range, name)

    class Over171(
        override val range: Pair<Int, Int?> = Pair(171, null),
        override val name: String = "171cm"
    ) : HeightQueryValue(range, name)

    fun asFilterEntry(): FilterEntry<HeightQueryValue> {
        return FilterEntry(
            name = name,
            value = this
        )
    }
}

sealed class WeightQueryValue(open val range: Pair<Int, Int?>, open val name: String) {
    class Below40(
        override val range: Pair<Int, Int?> = Pair(0, 40),
        override val name: String = "~40kg"
    ) : WeightQueryValue(range, name)

    class Below48(
        override val range: Pair<Int, Int?> = Pair(41, 48),
        override val name: String = "41~48kg"
    ) : WeightQueryValue(range, name)

    class Below56(
        override val range: Pair<Int, Int?> = Pair(49, 56),
        override val name: String = "49~56kg"
    ) : WeightQueryValue(range, name)

    class Over56(
        override val range: Pair<Int, Int?> = Pair(57, null),
        override val name: String = "57kg~"
    ) : WeightQueryValue(range, name)

    fun asFilterEntry(): FilterEntry<WeightQueryValue> {
        return FilterEntry(
            name = name,
            value = this
        )
    }
}