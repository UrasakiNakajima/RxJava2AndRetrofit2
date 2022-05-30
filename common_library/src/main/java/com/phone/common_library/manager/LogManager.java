package com.phone.common_library.manager;

import android.util.Log;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 10:17
 * introduce : 自定义长日志打印
 */

public class LogManager {

    private static boolean isOpenLog = true;
    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    public static void i(String TAG, String message) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.i(TAG + i, message.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.i(TAG, message.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void i(String TAG, String message, Throwable throwable) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.i(TAG + i, message.substring(start, end), throwable);
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.i(TAG, message.substring(start, strLength), throwable);
                    break;
                }
            }
        }
    }


    public static void e(String TAG, String message) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.e(TAG + i, message.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(TAG, message.substring(start, strLength));
                    break;
                }
            }
        }
    }

    public static void e(String TAG, String message, Throwable throwable) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.e(TAG + i, message.substring(start, end), throwable);
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    Log.e(TAG, message.substring(start, strLength), throwable);
                    break;
                }
            }
        }
    }

}
