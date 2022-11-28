package com.phone.common_library.custom_view.rclayout.helper

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.Checkable
import android.widget.FrameLayout
import com.phone.common_library.custom_view.rclayout.RCAttrs
import com.phone.common_library.custom_view.rclayout.RCHelper

open class RCFrameLayout(context: Context) : FrameLayout(context), Checkable, RCAttrs {

    private var mRCHelper: RCHelper

    init {
        mRCHelper = RCHelper(context, null)
        mRCHelper.initAttrs()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRCHelper.onSizeChanged(this, w, h)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(mRCHelper.mLayer, null, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        mRCHelper.onClipDraw(canvas)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        if (mRCHelper.mClipBackground) {
            canvas.save()
            canvas.clipPath(mRCHelper.mClipPath)
            super.draw(canvas)
            canvas.restore()
        } else {
            super.draw(canvas)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN && !mRCHelper.mAreaRegion.contains(
                ev.x.toInt(),
                ev.y.toInt()
            )
        ) {
            return false
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            refreshDrawableState()
        } else if (action == MotionEvent.ACTION_CANCEL) {
            isPressed = false
            refreshDrawableState()
        }
        return super.dispatchTouchEvent(ev)
    }

    //--- 公开接口 ----------------------------------------------------------------------------------

    //--- 公开接口 ----------------------------------------------------------------------------------
    override fun setClipBackground(clipBackground: Boolean) {
        mRCHelper.mClipBackground = clipBackground
        invalidate()
    }

    override fun setRoundAsCircle(roundAsCircle: Boolean) {
        mRCHelper.mRoundAsCircle = roundAsCircle
        invalidate()
    }

    override fun setRadius(radius: Int) {
        for (i in mRCHelper.radii.indices) {
            mRCHelper.radii[i] = radius.toFloat()
        }
        invalidate()
    }

    override fun setTopLeftRadius(topLeftRadius: Int) {
        mRCHelper.radii[0] = topLeftRadius.toFloat()
        mRCHelper.radii[1] = topLeftRadius.toFloat()
        invalidate()
    }

    override fun setTopRightRadius(topRightRadius: Int) {
        mRCHelper.radii[2] = topRightRadius.toFloat()
        mRCHelper.radii[3] = topRightRadius.toFloat()
        invalidate()
    }

    override fun setBottomLeftRadius(bottomLeftRadius: Int) {
        mRCHelper.radii[6] = bottomLeftRadius.toFloat()
        mRCHelper.radii[7] = bottomLeftRadius.toFloat()
        invalidate()
    }

    override fun setBottomRightRadius(bottomRightRadius: Int) {
        mRCHelper.radii[4] = bottomRightRadius.toFloat()
        mRCHelper.radii[5] = bottomRightRadius.toFloat()
        invalidate()
    }

    override fun setStrokeWidth(strokeWidth: Int) {
        mRCHelper.mStrokeWidth = strokeWidth
        invalidate()
    }

    override fun setStrokeColor(strokeColor: Int) {
        mRCHelper.mStrokeColor = strokeColor
        invalidate()
    }

    override fun invalidate() {
        mRCHelper.refreshRegion(this)
        super.invalidate()
    }

    override fun isClipBackground(): Boolean {
        return mRCHelper.mClipBackground
    }

    override fun isRoundAsCircle(): Boolean {
        return mRCHelper.mRoundAsCircle
    }

    override fun getTopLeftRadius(): Float {
        return mRCHelper.radii[0]
    }

    override fun getTopRightRadius(): Float {
        return mRCHelper.radii[2]
    }

    override fun getBottomLeftRadius(): Float {
        return mRCHelper.radii[4]
    }

    override fun getBottomRightRadius(): Float {
        return mRCHelper.radii[6]
    }

    override fun getStrokeWidth(): Int {
        return mRCHelper.mStrokeWidth
    }

    override fun getStrokeColor(): Int {
        return mRCHelper.mStrokeColor
    }


    //--- Selector 支持 ----------------------------------------------------------------------------

    //--- Selector 支持 ----------------------------------------------------------------------------
    override fun drawableStateChanged() {
        super.drawableStateChanged()
        mRCHelper.drawableStateChanged(this)
    }

    override fun setChecked(checked: Boolean) {
        if (mRCHelper.mChecked != checked) {
            mRCHelper.mChecked = checked
            refreshDrawableState()
            if (mRCHelper.mOnCheckedChangeListener != null) {
                mRCHelper.mOnCheckedChangeListener?.onCheckedChanged(this, mRCHelper.mChecked)
            }
        }
    }

    override fun isChecked(): Boolean {
        return mRCHelper.mChecked
    }

    override fun toggle() {
        isChecked = !mRCHelper.mChecked
    }

    fun setOnCheckedChangeListener(listener: RCHelper.OnCheckedChangeListener?) {
        mRCHelper.mOnCheckedChangeListener = listener
    }
}