package com.phone.library_common.manager

import androidx.core.content.ContextCompat
import com.phone.library_common.BaseApplication

object ResourcesManager {

    @JvmStatic
    fun getString(stringId: Int): String {
        return BaseApplication.instance().resources.getString(stringId)
    }

    @JvmStatic
    fun getColor(colorId: Int): Int {
//        return BaseApplication.instance().getResources().getColor(colorId);
        return ContextCompat.getColor(BaseApplication.instance(), colorId)
    }
}