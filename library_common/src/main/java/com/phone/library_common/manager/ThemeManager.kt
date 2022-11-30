package com.phone.library_common.manager

import com.phone.library_common.BaseApplication

class ThemeManager {

    fun getThemeColor(attrRes: Int): Int {
        val typedArray = BaseApplication.get().obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, 0xffffff)
        typedArray.recycle()
        return color
    }
}