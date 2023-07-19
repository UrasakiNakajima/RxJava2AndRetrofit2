package com.phone.library_glide.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableResource
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.phone.library_glide.GlideRoundTransform
import com.phone.library_glide.R
import java.io.File

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
object ImageLoaderManager {

    @JvmStatic
    fun display(context: Context, imageView: ImageView, url: String?, placeholder: Int, error: Int
    ) {
        url?.let {
            Glide.with(context).load(it).placeholder(placeholder)
                .error(error).into(imageView)
        }
    }

    @JvmStatic
    fun display(context: Context, imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(context).load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.library_picture30)
                .error(R.mipmap.library_picture26)
                .into(imageView)
        }
    }

    @JvmStatic
    fun displayCenterCrop(context: Context, imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(context).load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.library_picture30)
                .error(R.mipmap.library_picture26)
                .into(imageView)
        }
    }

    @JvmStatic
    fun display(context: Context, imageView: ImageView, file: File?) {
        if (file != null && file.exists()) {
            Glide.with(context).load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.library_ic_image_loading)
                .error(R.mipmap.library_ic_empty_picture)
                .into(imageView)
        }
    }

    @JvmStatic
    fun displayCenterCrop(context: Context, imageView: ImageView, file: File?) {
        if (file != null && file.exists()) {
            Glide.with(context).load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.library_ic_image_loading)
                .error(R.mipmap.library_ic_empty_picture)
                .into(imageView)
        }
    }

    @JvmStatic
    fun displaySmallPhoto(context: Context, imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(context).load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.library_ic_image_loading)
                .error(R.mipmap.library_ic_empty_picture)
                .thumbnail(0.5f)
                .into(imageView)
        }
    }

    @JvmStatic
    fun displayBigPhoto(context: Context, imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(context).load(it)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.library_ic_image_loading)
                .error(R.mipmap.library_ic_empty_picture)
                .into(imageView)
        }
    }

    @JvmStatic
    fun displayRound(context: Context, imageView: ImageView, url: String?) {
        url?.let {
            Glide.with(context).load(it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.library_toux2)
                .centerCrop().transform(GlideRoundTransformManager()).into(imageView)
        }
    }

    @JvmStatic
    fun displayRound(context: Context, imageView: ImageView, resourceId: Int) {
        Glide.with(context).load(resourceId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.library_toux2)
            .centerCrop().transform(GlideRoundTransformManager()).into(imageView)
    }

    @JvmStatic
    fun displayCorners(view: View, drawable: Drawable?) {
        drawable?.let {
            Glide.with(view)
                .load(it)
                .override(view.measuredWidth, view.measuredHeight)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        view.background = resource
                    }
                })
        }
    }

    @JvmStatic
    fun displayTransform(view: View, drawable: Drawable?, transform: Transformation<Bitmap>) {
        drawable?.let {
            Glide.with(view)
                .load(it)
                .transform(transform)
                .override(view.measuredWidth, view.measuredHeight)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        view.background = resource
                    }
                })
        }
    }

    @JvmStatic
    fun displayTransform(
        view: View,
        drawable: Drawable?,
        transform: Transformation<Bitmap>,
        cornerDipValue: Float
    ) {
        drawable?.let {
            Glide.with(view)
                .load(it)
                .transform(transform, RoundedCorners(cornerDipValue.toInt()))
                .override(view.measuredWidth, view.measuredHeight)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        view.background = resource
                    }
                })
        }
    }

    @JvmStatic
    fun displayAsDrawable(view: View, drawable: Drawable?) {
        drawable?.let {
            Glide.with(view)
                .asDrawable()
                .load(it)
                .transform(CenterCrop())
                .override(view.measuredWidth, view.measuredHeight)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        view.background = resource
                    }
                })
        }
    }

    @JvmStatic
    fun displayAsDrawable(view: View, drawable: Drawable?, cornerDipValue: Float) {
        drawable?.let {
            Glide.with(view)
                .asDrawable()
                .load(it)
                .transform(CenterCrop(), RoundedCorners(cornerDipValue.toInt()))
                .override(view.measuredWidth, view.measuredHeight)
                .into(object : CustomTarget<Drawable?>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        view.background = resource
                    }
                })
        }
    }


}