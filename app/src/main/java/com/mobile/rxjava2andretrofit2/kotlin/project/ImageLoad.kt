package com.mobile.rxjava2andretrofit2.kotlin.project

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.mobile.rxjava2andretrofit2.manager.LogManager


object ImageLoad {


//    companion object {

        val TAG = "ImageLoad";

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun bindImage(imageView: ImageView, url: String) {
//            Glide.with(imageView.context).load(url).into(imageView)
            var numberOfTimes: Int = 0;

            Glide.with(imageView.context).load(url)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            LogManager.i(TAG, "网络访问失败，请检查是否开始网络或者增加http的访问许可");
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            LogManager.i(TAG, "网络访问成功，可以显示图片");
                            return false
                        }

                    })
                    .into(object : ImageViewTarget<Drawable>(imageView) {

                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)
                            LogManager.i(TAG, "图片开始加载");
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            numberOfTimes++;
                            if (numberOfTimes == 1) {
                                Glide.with(imageView.context).load(url).into(imageView)
                            }
                            LogManager.i(TAG, "图片加载失败");
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            super.onResourceReady(resource, transition)
                            LogManager.i(TAG, "图片加载完成");
                            imageView.setImageDrawable(resource)
                        }

                        override fun setResource(resource: Drawable?) {
                            LogManager.i(TAG, "设置资源");
                        }
                    })
        }
//    }
}