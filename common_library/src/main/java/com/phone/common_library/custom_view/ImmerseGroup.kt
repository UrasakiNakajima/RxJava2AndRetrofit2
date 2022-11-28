package com.phone.common_library.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class ImmerseGroup(context: Context) : FrameLayout(context) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPadding(paddingLeft, paddingTop, paddingRight, 0)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}