package com.dabi.dabi.utils

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.dabi.dabi.EmptyFragment
import kotlin.math.roundToInt

fun <T : Parcelable> extractBundle(bundle: Bundle, key: String, clazz: Class<T>): T {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> bundle.getParcelable<T>(
            key,
            clazz,
        )!!
        else -> @Suppress("DEPRECATION")
        bundle.getParcelable<T>(EmptyFragment.argsKey)!!
    }
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()