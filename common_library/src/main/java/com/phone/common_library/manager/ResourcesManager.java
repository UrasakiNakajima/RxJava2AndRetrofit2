package com.phone.common_library.manager;

import androidx.core.content.ContextCompat;

import com.phone.common_library.BaseApplication;

public class ResourcesManager {

    public static String getString(int stringId) {
        return BaseApplication.getInstance().getResources().getString(stringId);
    }

    public static int getColor(int colorId) {
//        return BaseApplication.getInstance().getResources().getColor(colorId);
        return ContextCompat.getColor(BaseApplication.getInstance(), colorId);
    }
}
