package com.phone.common_library.manager;

import android.content.Context;
import android.content.res.TypedArray;

public class ThemeManager {

    public static int getThemeColor(Context context, int attrRes) {
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{attrRes});
        int color = typedArray.getColor(0, 0xffffff);
        typedArray.recycle();
        return color;
    }
}
