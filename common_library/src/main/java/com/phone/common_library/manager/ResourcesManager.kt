package com.phone.common_library.manager

import androidx.core.content.ContextCompat
import com.phone.common_library.BaseApplication

object ResourcesManager {

    fun getString(stringId: Int): String {
        return BaseApplication.get().resources.getString(stringId)
    }

    fun getColor(colorId: Int): Int {
//        return BaseApplication.get().getResources().getColor(colorId);
        return ContextCompat.getColor(BaseApplication.get(), colorId)
    }
}