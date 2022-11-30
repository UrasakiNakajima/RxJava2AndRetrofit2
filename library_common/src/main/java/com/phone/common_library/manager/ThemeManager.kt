package com.phone.common_library.manager

import com.phone.common_library.BaseApplication

class ThemeManager {

    fun getThemeColor(attrRes: Int): Int {
        val typedArray = BaseApplication.get().obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, 0xffffff)
        typedArray.recycle()
        return color
    }
}