package com.phone.library_common;

import android.content.Context;

public class JavaGetData {
    static {
        System.loadLibrary("return-data");
    }
    static {
        System.loadLibrary("native-lib");
    }

    public static native String nativeMethod(Context context, boolean isRelease);
    public static native String getStringFromNDK(Context context, boolean isRelease);

}
