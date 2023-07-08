package com.phone.library_glide.manager

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.phone.library_glide.R
import java.io.File

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
object ImageLoaderManager {

    @JvmStatic
    fun display(
        context: Context,
        imageView: ImageView,
        url: String,
        placeholder: Int,
        error: Int
    ) {
        Glide.with(context).load(url).placeholder(placeholder)
            .error(error).into(imageView)
    }

    @JvmStatic
    fun display(context: Context, imageView: ImageView, url: String) {
        Glide.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_empty_picture)
            .into(imageView)
    }

    @JvmStatic
    fun display(context: Context, imageView: ImageView, url: File) {
        Glide.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_empty_picture)
            .into(imageView)
    }

    @JvmStatic
    fun displaySmallPhoto(context: Context, imageView: ImageView, url: String) {
        Glide.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_empty_picture)
            .thumbnail(0.5f)
            .into(imageView)
    }

    @JvmStatic
    fun displayBigPhoto(context: Context, imageView: ImageView, url: String) {
        Glide.with(context).load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_empty_picture)
            .into(imageView)
    }

    @JvmStatic
    fun display(context: Context, imageView: ImageView, url: Int) {
        Glide.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.mipmap.ic_image_loading)
            .error(R.mipmap.ic_empty_picture)
            .into(imageView)
    }

    @JvmStatic
    fun displayRound(context: Context, imageView: ImageView, url: String) {
        Glide.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.library_toux2)
            .centerCrop().transform(GlideRoundTransformManager()).into(imageView)
    }

    @JvmStatic
    fun displayRound(context: Context, imageView: ImageView, resId: Int) {
        Glide.with(context).load(resId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.library_toux2)
            .centerCrop().transform(GlideRoundTransformManager()).into(imageView)
    }

}