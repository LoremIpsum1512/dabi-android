package com.dabi.dabi.utils

import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.dabi.dabi.R
import com.google.android.material.button.MaterialButton

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

@BindingAdapter("isSelected")
fun bingToggleButtonStyle(button: Button, isSelected: Boolean) {
    button.setTextColor(ContextCompat.getColor(button.context,R.color.white))
    if (isSelected) {
        button.setBackgroundColor(ContextCompat.getColor(button.context,R.color.black))

    }else {
        button.setBackgroundColor(ContextCompat.getColor(button.context,R.color.bg_gray))
    }
}

