package com.phone.library_custom_view.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class ImmerseGroup(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setPadding(paddingLeft, paddingTop, paddingRight, 0)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}