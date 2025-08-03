package com.phone.module_square.function_menu.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_glide.manager.ImageLoaderManager
import com.phone.module_square.R

/**
 * 活动进行中UI和动画
 */
class ActivityStartView : FrameLayout {

    companion object {
        private const val TAG = "ActivityStartView"
    }

    private val mLayoutActivityStart: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_activity_start) }
    private val mImvCenter: AppCompatImageView by lazy { findViewById<AppCompatImageView>(R.id.imv_center) }
    private val mTevCenter: AppCompatTextView by lazy { findViewById<AppCompatTextView>(R.id.tev_center) }
    private val mLayoutBezierLeft: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_bezier_left) }
    private val mLayoutBezierRight: FrameLayout by lazy { findViewById<FrameLayout>(R.id.layout_bezier_right) }

    private val mContext: Context
    private val mClickBtnIcons = mutableListOf<String>()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        mClickBtnIcons.add("Trump")
        mClickBtnIcons.add("Trump2")
        mClickBtnIcons.add("Trump3")
        initViews()

        LogManager.i(
            TAG,
            "getScreenWidth*****${ScreenManager.pxToDp(ScreenManager.getScreenWidth().toFloat())}"
        )
    }

    private fun initViews() {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.square_layout_activity_start, null)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        addView(view, layoutParams)

        ImageLoaderManager.display(
            mContext, mImvCenter, R.mipmap.library_picture24
        )
        mTevCenter.setOnClickListener {
            val bezierMoveViewRight = BezierMoveView(
                mContext,
                null,
                true,
                mClickBtnIcons
            )
            val bezierLayoutParamsRight =
                LayoutParams(
                    ScreenManager.getDimenPx(R.dimen.base_dp_140),
                    ScreenManager.getDimenPx(R.dimen.base_dp_240)
                )
            bezierMoveViewRight.animationEnd = object : BezierMoveView.AnimationEnd {
                override fun onAnimationEnd() {
                    mLayoutBezierRight.removeView(bezierMoveViewRight)
                }
            }
            mLayoutBezierRight.addView(bezierMoveViewRight, bezierLayoutParamsRight)
            bezierMoveViewRight.startAnimator()

            val bezierMoveViewLeft = BezierMoveView(
                mContext,
                null,
                false,
                mClickBtnIcons
            )
            val bezierLayoutParamsLeft =
                LayoutParams(
                    ScreenManager.getDimenPx(R.dimen.base_dp_140),
                    ScreenManager.getDimenPx(R.dimen.base_dp_240)
                )
            bezierMoveViewLeft.animationEnd = object : BezierMoveView.AnimationEnd {
                override fun onAnimationEnd() {
                    mLayoutBezierLeft.removeView(bezierMoveViewLeft)
                }
            }
            mLayoutBezierLeft.addView(bezierMoveViewLeft, bezierLayoutParamsLeft)
            bezierMoveViewLeft.startAnimator()
            phoneVibrate()

            onSummonedCallBack?.onSummoned()
        }

        mImvCenter.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchStartAnimator()
            } else if (event.action == MotionEvent.ACTION_UP) {
                touchEndAnimator()
            }
            false
        }
    }

    private fun phoneVibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)
    }

    internal var onSummonedCallBack: OnSummonedCallBack? = null

    internal interface OnSummonedCallBack {
        fun onSummoned()
    }

    private fun touchStartAnimator() {
        // 创建ObjectAnimator，设置目标视图，属性名，和动画执行的时长及放大的比例
        val scaleX = ObjectAnimator.ofFloat(mImvCenter, "scaleX", 1.0f, 0.95f)
        val scaleY = ObjectAnimator.ofFloat(mImvCenter, "scaleY", 1.0f, 0.95f)
        scaleX.duration = 100 // 缩小动画时长100毫秒
        scaleY.duration = 100 // 缩小动画时长100毫秒
        // 将缩放动画组合起来
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleY)
        // 启动动画
        animatorSet.start()
    }

    private fun touchEndAnimator() {
        // 创建ObjectAnimator，设置目标视图，属性名，和动画执行的时长及放大的比例
        val scaleX = ObjectAnimator.ofFloat(mImvCenter, "scaleX", 0.95f, 1.05f)
        val scaleY = ObjectAnimator.ofFloat(mImvCenter, "scaleY", 0.95f, 1.05f)
        scaleX.duration = 50 // 放大动画时长50毫秒
        scaleY.duration = 50 // 缩小动画时长50毫秒

        // 创建ObjectAnimator，设置目标视图，属性名，和动画执行的时长及放大的比例
        val scaleX2 = ObjectAnimator.ofFloat(mImvCenter, "scaleX", 1.05f, 1f)
        val scaleY2 = ObjectAnimator.ofFloat(mImvCenter, "scaleY", 1.05f, 1f)
        scaleX2.duration = 50 // 放大动画时长50毫秒
        scaleY2.duration = 50 // 缩小动画时长50毫秒
        // 将缩放动画组合起来
        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleY).after(scaleX2).with(scaleY2)
        // 启动动画
        animatorSet.start()
    }

    private fun setTevCallNumber(tevCallNumber: TextView, totalLikeCount: Long) {
        val clickNumberStr = setClickNumber(totalLikeCount)
        tevCallNumber.text = clickNumberStr
    }

    private fun setClickNumber(clickNumber: Long): String {
        val totalLikeCountStr = clickNumber.toString()
        val arr = totalLikeCountStr.split("")
        if (arr.isNotEmpty()) {
            val strList = mutableListOf<String>()
            for (index in arr.size - 1 downTo 0) {
                if (!arr[index].isEmpty()) {
                    strList.add(arr[index])
                }
            }
            val numberStringBuilder = StringBuilder()
            for (index in 0 until strList.size) {
                numberStringBuilder.append(strList[index])
                val currentIndex = (index + 1)
                if (currentIndex % 3 == 0 && currentIndex < strList.size) {
                    numberStringBuilder.append(",")
                }
            }
            val numberStr = numberStringBuilder.toString()
            val newArr = numberStr.split("")
            if (newArr.isNotEmpty()) {
                val newStrList = mutableListOf<String>()
                for (index in newArr.size - 1 downTo 0) {
                    if (!newArr[index].isEmpty()) {
                        newStrList.add(newArr[index])
                    }
                }
                val newNumberStr = StringBuilder()
                for (index in 0 until newStrList.size) {
                    newNumberStr.append(newStrList[index])
                }
                return newNumberStr.toString()
            }
            return ""
        }
        return ""
    }

}