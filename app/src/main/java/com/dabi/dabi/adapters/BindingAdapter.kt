package com.dabi.dabi.utils

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.dabi.dabi.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgUrl.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {

                placeholder(
                    ColorDrawable(
                        imgView.resources.getColor(
                            R.color.bg_gray,
                            imgView.context.theme
                        )
                    )
                )
            }
        }
    }
}