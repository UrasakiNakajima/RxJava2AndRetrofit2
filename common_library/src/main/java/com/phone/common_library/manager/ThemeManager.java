package com.phone.common_library.manager;

import android.content.Context;
import android.content.res.TypedArray;

import com.phone.common_library.BaseApplication;

public class ThemeManager {

    public static int getThemeColor(int attrRes) {
        TypedArray typedArray = BaseApplication.getInstance().obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }
}
