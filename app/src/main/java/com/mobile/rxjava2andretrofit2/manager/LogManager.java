package com.mobile.rxjava2andretrofit2.manager;

import android.content.Context;
import android.util.Log;

import com.mobile.rxjava2andretrofit2.MineApplication;
import com.mobile.rxjava2andretrofit2.R;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 10:17
 * introduce : 自定义长日志打印
 */

public class LogManager {

    public static void log(String s) {
        Log.i("test", s);
    }

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

    public static void i(Context context, String TAG, String message) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            String info;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    info = message.substring(start, end);
                    Log.i(TAG + i, message.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    info = message.substring(start, strLength);
                    Log.i(TAG, info);
                    break;
                }
            }
        }
    }

    public static void w(Context context, String TAG, String message) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            String warn;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    warn = message.substring(start, end);
                    Log.w(TAG + i, message.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    warn = message.substring(start, strLength);
                    Log.w(TAG, warn);
                    break;
                }
            }
        }
    }

    public static void e(Context context, String TAG, String message) {
        if (isOpenLog) {
            int strLength = message.length();
            int start = 0;
            int end = LOG_MAXLENGTH;
            String error;
            for (int i = 0; i < 100; i++) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    error = message.substring(start, end);
                    Log.e(TAG + i, message.substring(start, end));
                    start = end;
                    end = end + LOG_MAXLENGTH;
                } else {
                    error = message.substring(start, strLength);
                    Log.e(TAG, error);
                    break;
                }
            }
        }
    }

}
