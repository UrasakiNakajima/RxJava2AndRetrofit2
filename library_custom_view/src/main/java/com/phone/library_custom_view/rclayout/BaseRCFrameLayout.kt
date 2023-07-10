package com.phone.call_third_party_so.custom_view.rclayout.helper

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

abstract class BaseRCFrameLayout(context: Context, attrs: AttributeSet?) :
    RCFrameLayout(context, attrs) {

    private val TAG = BaseRCFrameLayout::class.java.simpleName
    private var mActivity: Activity
    protected var bodyParams: Map<String, String>? = null

    init {
        mActivity = context as Activity
        bodyParams = HashMap()
    }

    abstract fun initData()

    abstract fun initViews()

    private var mStartAnimator = false

    /**
     * 设置推荐位到达边界时抖动
     *
     * @param focusView
     * @param direction 抖动方向，取值使用[View.FOCUS_RIGHT],[View.FOCUS_LEFT],
     * [View.FOCUS_UP],[View.FOCUS_DOWN]
     */
    protected open fun setShakeAnimator(focusView: View?, direction: Int) {
        val translationAnimator: ObjectAnimator
        translationAnimator = when (direction) {
            FOCUS_LEFT -> ObjectAnimator.ofFloat(
                focusView,
                "translationX",
                0f,
                -15f,
                0f,
                15f,
                0f,
                -15f,
                0f,
                15f,
                0f
            )
            FOCUS_DOWN -> ObjectAnimator.ofFloat(
                focusView,
                "translationY",
                0f,
                15f,
                0f,
                -15f,
                0f,
                15f,
                0f,
                -15f,
                0f
            )
            FOCUS_UP -> ObjectAnimator.ofFloat(
                focusView,
                "translationY",
                0f,
                -15f,
                0f,
                15f,
                0f,
                -15f,
                0f,
                15f,
                0f
            )
            FOCUS_RIGHT -> ObjectAnimator.ofFloat(
                focusView,
                "translationX",
                0f,
                15f,
                0f,
                -15f,
                0f,
                15f,
                0f,
                -15f,
                0f
            )
            else -> ObjectAnimator.ofFloat(
                focusView,
                "translationX",
                0f,
                15f,
                0f,
                -15f,
                0f,
                15f,
                0f,
                -15f,
                0f
            )
        }
        translationAnimator.interpolator = LinearInterpolator()
        translationAnimator.duration = 300
        translationAnimator.addListener(animatorListener)
        translationAnimator.start()
    }

    private val animatorListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            mStartAnimator = true
        }

        override fun onAnimationEnd(animation: Animator) {
            mStartAnimator = false
        }

        override fun onAnimationCancel(animation: Animator) {
            mStartAnimator = false
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    protected open fun isShakeAnimationRun(): Boolean {
        return mStartAnimator
    }

}