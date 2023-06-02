package com.phone.library_common;

import android.content.Context;

public class JavaGetData {
    static {
        System.loadLibrary("return-data");
        System.loadLibrary("native-lib");
    }

    /**
     * 这里的nativeAesKey 法按说不能直接调用return-data.cpp文件的
     * Java_com_phone_library_1common_JavaGetData_nativeAesKey方法，
     * 只能调用lib文件夹下的return-data.so动态库文件的方法，
     * 可能是因为和library_common写在同一个Project下，
     * 因此点击nativeAesKey方法，可以直接跳到
     * return-data.cpp文件的Java_com_phone_library_1common_JavaGetData_nativeAesKey方法（有点奇怪）
     *
     * @param context
     * @param isRelease
     * @return
     */
    public static native String nativeAesKey(Context context, boolean isRelease);

    /**
     * 这里的nativeDatabaseEncryptKey方法按说不能直接调用return-data.cpp文件的
     * Java_com_phone_library_1common_JavaGetData_nativeDatabaseEncryptKey方法，
     * 只能调用lib文件夹下的return-data.so动态库文件的方法，
     * 可能是因为和library_common写在同一个Project下，
     * 因此点击nativeDatabaseEncryptKey方法，可以直接跳到
     * return-data.cpp文件的Java_com_phone_library_1common_JavaGetData_nativeDatabaseEncryptKey方法（有点奇怪）
     *
     * @param context
     * @param isRelease
     * @return
     */
    public static native String nativeDatabaseEncryptKey(Context context, boolean isRelease);

    /**
     * 这里的nativeAesKey方法按说不能直接调用native-lib.cpp文件的
     * Java_com_phone_library_1common_JavaGetData_nativeGetString方法，
     * 只能调用lib文件夹下的native-lib.so动态库文件的方法，
     * 可能是因为和library_common写在同一个Project下，
     * 因此点击nativeAesKey方法，可以直接跳到
     * native-lib.cpp文件的Java_com_phone_library_1common_JavaGetData_nativeGetString方法（有点奇怪）
     *
     * @param context
     * @param isRelease
     * @return
     */
    public static native String nativeGetString(Context context, boolean isRelease);


}
