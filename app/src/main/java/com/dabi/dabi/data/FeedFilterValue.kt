package com.dabi.dabi.data

sealed class HeightFilterValue {
    class none()
    class below150(val range: Pair<Int, Int> = Pair(0, 150), val name: String = "~150cm")
    class below155(val range: Pair<Int, Int> = Pair(151, 155), val name: String = "1151~155cm")


}