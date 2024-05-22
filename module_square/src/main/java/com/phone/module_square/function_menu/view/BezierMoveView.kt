package com.phone.module_square.function_menu.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.phone.library_base.manager.ScreenManager.getDimenPx
import com.phone.library_glide.manager.ImageLoaderManager
import com.phone.module_square.R
import com.phone.module_square.function_menu.evaluator.BezierEvaluator
import java.util.Random


/**
 * 贝塞尔曲线动画
 *
 * @author Friedrich
 * @copyright
 * @date 2024-04-30$ 18:22:00$
 **/
class BezierMoveView(
    context: Context,
    attrs: AttributeSet?,
    private val isRightBezierAnimator: Boolean,
    val clickBtnIcons: List<String>
) :
    FrameLayout(context, attrs) {

    companion object {
        private const val TAG = "BezierImageView"
    }

    private var mImvSize = 0
    private val mHeight = getDimenPx(R.dimen.base_dp_240)
    private val mWidth = getDimenPx(R.dimen.base_dp_140)
    private lateinit var mImageView: AppCompatImageView
    private val mMargin = getDimenPx(R.dimen.base_dp_20)

    init {
        initViews(context)
    }

    private fun initViews(context: Context) {
        if (clickBtnIcons.isEmpty()) {
            return
        }
        mImvSize = getDimenPx(R.dimen.base_dp_30)
        val marginTop = mHeight - mImvSize
        val marginBottom = getDimenPx(R.dimen.base_dp_20)
        mImageView = AppCompatImageView(context)
        val drawableRandom = Random()
        val index = drawableRandom.nextInt(clickBtnIcons.size)
        if (index <= clickBtnIcons.size - 1) {
            val url = clickBtnIcons[index]
            if (!TextUtils.isEmpty(url)) {
                ImageLoaderManager.display(context, mImageView, R.mipmap.library_red_heart)
            }
        }
        val layoutParams = LayoutParams(mImvSize, mImvSize)
        layoutParams.topMargin = marginTop
        layoutParams.bottomMargin = marginBottom
        if (isRightBezierAnimator) {
            layoutParams.gravity = Gravity.START
        } else {
            layoutParams.gravity = Gravity.END
        }
        addView(mImageView, layoutParams)
    }

    private fun getRightBezierValueAnimator(target: View): ValueAnimator {
        val controlP0 = PointF(mMargin.toFloat(), (mHeight - mImvSize).toFloat())
        val controlP3 = PointF((mWidth - mImvSize - mMargin).toFloat(), mImvSize.toFloat())
        val x1Random = Random()
        val x1 = x1Random.nextInt(mWidth - mImvSize)
        val y1Random = Random()
        val y1 = y1Random.nextInt(mHeight - mImvSize)
        val x2Random = Random()
        val x2 = x2Random.nextInt(mWidth - mImvSize)
        val y2Random = Random()
        val y2 = y2Random.nextInt(mHeight - mImvSize)
        val controlP1 = PointF(x1.toFloat(), y1.toFloat())
        val controlP2 = PointF(x2.toFloat(), y2.toFloat())
        // 初始化贝塞尔估值器
        val evaluator = BezierEvaluator(controlP1, controlP2)
        // 起点在底部中心位置，终点在底部随机一个位置
        val animator = ValueAnimator.ofObject(evaluator, controlP0, controlP3)
        animator.setTarget(target)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            val pointF =
                valueAnimator.animatedValue as PointF
            target.x = pointF.x
            target.y = pointF.y
        }
        return animator
    }

    private fun getLeftBezierValueAnimator(target: View): ValueAnimator {
        val controlP0 =
            PointF((mWidth - mImvSize - mMargin).toFloat(), (mHeight - mImvSize).toFloat())
        val controlP3 = PointF((-mMargin).toFloat(), mImvSize.toFloat())
        val x1Random = Random()
        val x1 = x1Random.nextInt(mWidth - mImvSize)
        val y1Random = Random()
        val y1 = y1Random.nextInt(mHeight - mImvSize)
        val x2Random = Random()
        val x2 = x2Random.nextInt(mWidth - mImvSize)
        val y2Random = Random()
        val y2 = y2Random.nextInt(mHeight - mImvSize)
        val controlP1 = PointF(x1.toFloat(), y1.toFloat())
        val controlP2 = PointF(x2.toFloat(), y2.toFloat())
        // 初始化贝塞尔估值器
        val evaluator = BezierEvaluator(controlP1, controlP2)
        // 起点在底部中心位置，终点在底部随机一个位置
        val animator = ValueAnimator.ofObject(evaluator, controlP0, controlP3)
        animator.setTarget(target)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            val pointF =
                valueAnimator.animatedValue as PointF
            target.x = pointF.x
            target.y = pointF.y
        }
        return animator
    }

    fun startAnimator() {
        if (clickBtnIcons.isEmpty()) {
            return
        }
        val alpha = ObjectAnimator.ofFloat(mImageView, ALPHA, 1f, 0f)
        val scaleX = ObjectAnimator.ofFloat(mImageView, SCALE_X, 1f, 2f)
        val scaleY = ObjectAnimator.ofFloat(mImageView, SCALE_Y, 1f, 2f)
        val rotation = ObjectAnimator.ofFloat(mImageView, ROTATION, -90f, 90f)
        val animatorSet = AnimatorSet()
        animatorSet.setTarget(mImageView)
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.setDuration(1000).playTogether(alpha, scaleX, scaleY, rotation)
        val bezierValueAnimator: ValueAnimator = if (isRightBezierAnimator) {
            getRightBezierValueAnimator(mImageView) //贝塞尔曲线路径动画
        } else {
            getLeftBezierValueAnimator(mImageView) //贝塞尔曲线路径动画
        }
        val finalSet = AnimatorSet()
        finalSet.playTogether(animatorSet, bezierValueAnimator)
        finalSet.interpolator = LinearInterpolator()
        finalSet.duration = 1000
        finalSet.setTarget(mImageView)
        finalSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                removeView(mImageView) //移除图片
                animationEnd?.onAnimationEnd()
            }
        })
        finalSet.start()
    }

    internal var animationEnd: AnimationEnd? = null

    internal interface AnimationEnd {
        fun onAnimationEnd()
    }

}
