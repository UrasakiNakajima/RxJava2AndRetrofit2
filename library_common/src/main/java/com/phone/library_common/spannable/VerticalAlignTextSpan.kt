package com.phone.library_common.spannable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.text.TextPaint
import com.phone.library_base.manager.LogManager
import com.phone.library_common.manager.ScreenManager.spToPx

class VerticalAlignTextSpan(fontSizeSp: Float) {

    private val TAG = VerticalAlignTextSpan::class.java.simpleName
    private var fontSizeSp = -1f //单位:sp

    fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: FontMetricsInt?): Int {
        val newPaint: Paint = getCustomTextPaint(paint)
        return newPaint.measureText(text, start, end).toInt()
    }

    fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val newPaint: Paint = getCustomTextPaint(paint)
        val fontMetricsInt = newPaint.fontMetricsInt
        val offsetY =
            (y + fontMetricsInt.ascent + y + fontMetricsInt.descent) / 2 - (top + bottom) / 2
        LogManager.i(TAG, "offsetY-> $offsetY")
        canvas.drawText(text!!, start, end, x, (y - offsetY).toFloat(), newPaint)
    }

    private fun getCustomTextPaint(srcPaint: Paint): TextPaint {
        val textPaint = TextPaint(srcPaint)
        if (fontSizeSp != -1f) { //-1没有重设fontSize
            textPaint.textSize = spToPx(fontSizeSp).toFloat() //sp转px
        }
        return textPaint
    }

}