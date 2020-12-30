package com.mobile.rxjava2andretrofit2.kotlin.project

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


class ImageLoad {

    companion object {
        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun bindImage(imageView: ImageView, url: String) {
            Glide.with(imageView.getContext()).load(url).into(imageView)
        }
    }
}