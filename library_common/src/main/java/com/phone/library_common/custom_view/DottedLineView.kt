package com.phone.library_common.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.phone.library_common.R

class DottedLineView(context: Context) : View(context) {

    private val TAG = DottedLineView::class.java.simpleName
    private val mDashPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRect = Rect()

    init {
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val metrics = resources.displayMetrics
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics)
        val dashGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, metrics)
        val dashWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, metrics)
        mDashPaint.color = ContextCompat.getColor(context, R.color.color_FF3258F7)
        mDashPaint.style = Paint.Style.STROKE
        mDashPaint.strokeWidth = width
        mDashPaint.isAntiAlias = true
        //DashPathEffect是Android提供的虚线样式API，具体的使用可以参考下面的介绍
        mDashPaint.pathEffect = DashPathEffect(floatArrayOf(dashWidth, dashGap), 0F)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //取出线条的位置（位置的定义放在XML的layout中，具体如下xml文件所示）
        mRect.left = left
        mRect.top = top
        mRect.right = right
        mRect.bottom = bottom
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val metrics = resources.displayMetrics
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics)
        val dashGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, metrics)
        val dashWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, metrics)
        mDashPaint.color = ContextCompat.getColor(context, R.color.color_FF3258F7)
        mDashPaint.style = Paint.Style.STROKE
        mDashPaint.strokeWidth = width
        mDashPaint.isAntiAlias = true
        //DashPathEffect是Android提供的虚线样式API，具体的使用可以参考下面的介绍
        mDashPaint.pathEffect = DashPathEffect(floatArrayOf(dashWidth, dashGap), 0F)
        val x0 = (mRect.right - mRect.left) / 2f
        val y0 = 0f
        val y1 = y0 + mRect.bottom - mRect.top
        canvas.drawLine(x0, y0, x0, y1, mDashPaint)
    }
}