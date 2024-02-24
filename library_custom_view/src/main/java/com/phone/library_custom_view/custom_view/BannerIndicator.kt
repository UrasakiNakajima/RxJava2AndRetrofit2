package com.phone.library_custom_view.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.phone.library_custom_view.R

class BannerIndicator(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        private const val TAG = "BannerIndicator"
    }

    private var number = 0
    private var position = 0
    private val mPaint = Paint()
    private var selectColor = 0
    private var unselectColor = 0
    private var radius = 0f
    private var space = 0f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerIndicator)
        selectColor = typedArray.getColor(R.styleable.BannerIndicator_selectColor, Color.RED)
        unselectColor =
            typedArray.getColor(R.styleable.BannerIndicator_unselectedColor, Color.WHITE)
        radius = typedArray.getDimension(R.styleable.BannerIndicator_radius, 8f)
        space = typedArray.getDimension(R.styleable.BannerIndicator_space, 16f)
        typedArray.recycle()

        mPaint.style = Paint.Style.FILL;
        mPaint.isAntiAlias = true;
        mPaint.isFilterBitmap = true;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val startPosition = width.toFloat() / 2 - (radius * 2 * number + space * (number - 1)) / 2
        canvas.save()
        for (i in 0 until number) {
            if (i == position) {
                mPaint.color = selectColor
            } else {
                mPaint.color = unselectColor
            }
            canvas.drawCircle(
                startPosition + radius * (2 * i + 1) + i * space,
                height.toFloat() / 2,
                radius,
                mPaint
            )
        }
        canvas.restore()
    }

    fun setNumber(number: Int) {
        this.number = number
    }

    fun setPosition(position: Int) {
        this.position = position
        invalidate()
    }

}