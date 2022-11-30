package com.phone.common_library.glide_binding_adapter

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.phone.common_library.R


object CommonBindingAdapter {

    val TAG = CommonBindingAdapter::class.java.simpleName

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        if (!TextUtils.isEmpty(url)) {
//            GlideApp.with(imageView.context)
//                .load(url)
//                .error(GlideApp.with(imageView.context).load(url))
//                .into(imageView)
        }

//        var numberOfTimes: Int = 0;
//            GlideApp.with(imageView.context).load(url)
//                    .listener(object : RequestListener<Drawable> {
//                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
//                            LogManager.i(TAG, "网络访问失败，请检查是否开始网络或者增加http的访问许可");
//                            return false
//                        }
//
//                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                            LogManager.i(TAG, "网络访问成功，可以显示图片");
//                            return false
//                        }
//
//                    })
//                    .error(GlideApp.with(imageView.context).load(url))
//                    .into(object : ImageViewTarget<Drawable>(imageView) {
//
//                        override fun onLoadStarted(placeholder: Drawable?) {
//                            super.onLoadStarted(placeholder)
//                            LogManager.i(TAG, "图片开始加载");
//                        }
//
//                        override fun onLoadFailed(errorDrawable: Drawable?) {
//                            super.onLoadFailed(errorDrawable)
////                            numberOfTimes++;
////                            if (numberOfTimes == 1) {
////                                GlideApp.with(imageView.context).load(url).into(imageView)
////                            }
//                            LogManager.i(TAG, "图片加载失败");
//                        }
//
//                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
//                            super.onResourceReady(resource, transition)
//                            LogManager.i(TAG, "图片加载完成");
//                            imageView.setImageDrawable(resource)
//                        }
//
//                        override fun setResource(resource: Drawable?) {
//                            LogManager.i(TAG, "设置资源");
//                        }
//                    })
    }


    /**
     * 加载图片,做高斯模糊处理
     */

    @JvmStatic
    @BindingAdapter("articleCollect")
    fun imgPlayBlur(view: ImageView, collect: Boolean) {
        if (collect) {
            view.setImageResource(R.mipmap.collect)
        } else {
            view.setImageResource(R.mipmap.un_collect)
        }
    }

}