package com.phone.library_common.manager

import com.phone.library_base.BaseApplication

object ThemeManager {

    @JvmStatic
    fun getThemeColor(attrRes: Int): Int {
        val typedArray = BaseApplication.instance().obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, 0xffffff)
        typedArray.recycle()
        return color
    }
}