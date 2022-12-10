package com.phone.library_common.manager

import androidx.core.content.ContextCompat
import com.phone.library_common.BaseApplication

object ResourcesManager {

    @JvmStatic
    fun getString(stringId: Int): String {
        return BaseApplication.get().resources.getString(stringId)
    }

    @JvmStatic
    fun getColor(colorId: Int): Int {
//        return BaseApplication.get().getResources().getColor(colorId);
        return ContextCompat.getColor(BaseApplication.get(), colorId)
    }
}