package com.phone.library_common.manager

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.phone.library_common.custom_view.GlideRoundTransform

object GlideRoundManager {

    @JvmStatic
    fun setRoundCorner(view: View, resourceId: Drawable?, cornerDipValue: Float) {
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
                        Glide.with(view)
                            .asDrawable()
                            .load(resourceId)
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
                })
            } else {
                Glide.with(view)
                    .asDrawable()
                    .load(resourceId)
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
                        Glide.with(view)
                            .load(resourceId)
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
                })
            } else {
                Glide.with(view)
                    .load(resourceId)
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

    @JvmStatic
    fun setCorners(
        view: View,
        resourceId: Drawable?,
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
                        Glide.with(view)
                            .load(resourceId)
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
                })
            } else {
                Glide.with(view)
                    .load(resourceId)
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
                        Glide.with(view)
                            .load(resourceId)
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
                })
            } else {
                val transform = GlideRoundTransform(
                    view.context,
                    leftTop_corner,
                    leftBottom_corner,
                    rightTop_corner,
                    rightBottom_corner
                )
                Glide.with(view)
                    .load(resourceId)
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
    }

}