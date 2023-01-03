package com.phone.library_common;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

public class JavaGetData {
    static {
        System.loadLibrary("return-data");
        System.loadLibrary("native-lib");
    }

    public static void loadData(){
        SQLiteDatabase.loadLibs(BaseApplication.get());
    }

    public static native String nativeAesKey(Context context, boolean isRelease);
    public static native String nativeGetString(Context context, boolean isRelease);

}
