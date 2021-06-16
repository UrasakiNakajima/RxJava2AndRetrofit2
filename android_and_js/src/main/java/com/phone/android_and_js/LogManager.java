package com.phone.android_and_js;

import android.util.Log;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
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

}
