package com.phone.library_common.manager

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import com.phone.library_common.manager.ScreenManager.spToPx
import com.phone.library_common.spannable.VerticalAlignTextSpan

object TextViewStyleManager {

    private val TAG = TextViewStyleManager::class.java.simpleName

    @JvmStatic
    fun setTextViewStyle(
        textView: TextView,
        data: String,
        start: Int
    ) {
        val spannable: Spannable = SpannableString(data)
        // 粗体
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            data.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        // 显示
        textView.text = spannable
    }

    @JvmStatic
    fun setTextViewStyle(
        textView: TextView,
        data: String?,
        start: Int,
        end: Int,
        size: Float
    ) {
        val spannable: Spannable = SpannableString(data)
        // 粗体
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        spannable.setSpan(
            AbsoluteSizeSpan(spToPx(size)),
            start,
            end,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        // 显示
        textView.text = spannable
    }

    @JvmStatic
    fun setTextViewStyleVerticalCenter(
        textView: TextView,
        data: String?,
        start: Int,
        end: Int,
        size: Float
    ) {
        val spannable: Spannable = SpannableString(data)
        // 粗体
        spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        spannable.setSpan(
            AbsoluteSizeSpan(spToPx(size)),
            start,
            end.toInt(),
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        //SpannableString字体大小不一致垂直居中显示
        spannable.setSpan(
            VerticalAlignTextSpan(size),
            start,
            end,
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        // 显示
        textView.text = spannable
    }

}