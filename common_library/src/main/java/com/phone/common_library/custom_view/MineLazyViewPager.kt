package com.phone.common_library.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.phone.common_library.custom_view.LazyViewPager

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 14:12
 * introduce : 禁止左右滑动
 */

class MineLazyViewPager(context: Context, attrs: AttributeSet) : LazyViewPager(context, attrs) {

    private val isCanScroll = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (isCanScroll) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (isCanScroll) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

}