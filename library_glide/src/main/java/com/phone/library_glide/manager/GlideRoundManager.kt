package com.phone.library_glide.manager

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.phone.library_glide.GlideRoundTransform

object GlideRoundManager {

    @JvmStatic
    fun setRoundCorner(view: View, drawable: Drawable?, cornerDipValue: Float) {
        if (cornerDipValue == 0f) {
            if (view.measuredWidth == 0 && view.measuredHeight == 0) {
                view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        view.removeOnLayoutChangeListener(this)
                        ImageLoaderManager.displayAsDrawable(view, drawable)
                    }
                })
            } else {
                ImageLoaderManager.displayAsDrawable(view, drawable)
            }
        } else {
            if (view.measuredWidth == 0 && view.measuredHeight == 0) {
                view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        view.removeOnLayoutChangeListener(this)

                        ImageLoaderManager.displayTransform(view, drawable, CenterCrop(), cornerDipValue)
                    }
                })
            } else {
                ImageLoaderManager.displayTransform(view, drawable, CenterCrop(), cornerDipValue)
            }
        }
    }

    @JvmStatic
    fun setCorners(
        view: View,
        drawable: Drawable?,
        leftTop_corner: Float,
        leftBottom_corner: Float,
        rightTop_corner: Float,
        rightBottom_corner: Float
    ) {
        if (leftTop_corner == 0f && leftBottom_corner == 0f && rightTop_corner == 0f && rightBottom_corner == 0f) {
            if (view.measuredWidth == 0 && view.measuredHeight == 0) {
                view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        view.removeOnLayoutChangeListener(this)
                        ImageLoaderManager.displayCorners(view, drawable)
                    }
                })
            } else {
                ImageLoaderManager.displayCorners(view, drawable)
            }
        } else {
            if (view.measuredWidth == 0 && view.measuredHeight == 0) {
                view.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                    override fun onLayoutChange(
                        v: View,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {
                        view.removeOnLayoutChangeListener(this)
                        val transform = GlideRoundTransform(
                            view.context,
                            leftTop_corner,
                            leftBottom_corner,
                            rightTop_corner,
                            rightBottom_corner
                        )
                        ImageLoaderManager.displayTransform(view, drawable, transform)
                    }
                })
            } else {
                val transform = GlideRoundTransform(
                    view.context,
                    leftTop_corner,
                    leftBottom_corner,
                    rightTop_corner,
                    rightBottom_corner
                )
                ImageLoaderManager.displayTransform(view, drawable, transform)
            }
        }
    }

}