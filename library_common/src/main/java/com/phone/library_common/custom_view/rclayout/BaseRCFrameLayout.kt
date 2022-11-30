package com.phone.library_common.custom_view.rclayout.helper

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast

abstract class BaseRCFrameLayout(context: Context) : RCFrameLayout(context) {

    private val TAG = "BaseRCFrameLayout"
    private var mActivity: Activity? = null
    protected var bodyParams: Map<String, String>? = null
    private var intent: Intent? = null
    private var bundle: Bundle? = null

    init {
        mActivity = context as Activity
        bodyParams = HashMap()
    }

    protected abstract fun initLayout(): View?

    protected abstract fun initData()

    protected abstract fun initViews()

    protected open fun initViews(view: View?) {}

    protected open fun initLoadData() {}

    open fun showLoading() {}

    open fun hideLoading() {}

    protected open fun showToast(message: String?) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show()
    }

    protected open fun startActivityCarryParams(cls: Class<*>?, params: Map<String?, String?>?) {
        intent = Intent(mActivity, cls)
        bundle = Bundle()
        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) { //如果参数不是null，才把参数传给后台
                    bundle!!.putString(key, params[key])
                }
            }
            intent!!.putExtras(bundle!!)
        }
        mActivity!!.startActivity(intent)
    }

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