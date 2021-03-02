package com.mobile.rxjava2andretrofit2.kotlin.square

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object CommonBindingAdapter2 {

    val TAG = "CommonBindingAdapter2"

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String?) {

        if (url != null) {
            Glide.with(imageView.context)
                    .load(url)
                    .error(Glide.with(imageView.context).load(url))
                    .into(imageView)
        }
    }

}