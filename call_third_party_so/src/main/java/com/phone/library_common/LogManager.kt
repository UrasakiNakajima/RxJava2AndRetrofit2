package com.phone.library_common

import android.util.Log

object LogManager {

    private val isOpenLog = !BuildConfig.IS_RELEASE

    //规定每段显示的长度
    private val LOG_MAXLENGTH = 2000

    @JvmStatic
    fun i(TAG: String, message: String) {
        if (isOpenLog) {
            val strLength = message.length
            var start = 0
            var end = LOG_MAXLENGTH
            for (i in 0..99) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.i(TAG + i, message.substring(start, end))
                    start = end
                    end = end + LOG_MAXLENGTH
                } else {
                    Log.i(TAG, message.substring(start, strLength))
                    break
                }
            }
        }
    }

    @JvmStatic
    fun i(TAG: String, message: String, throwable: Throwable?) {
        if (isOpenLog) {
            val strLength = message.length
            var start = 0
            var end = LOG_MAXLENGTH
            for (i in 0..99) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.i(TAG + i, message.substring(start, end), throwable)
                    start = end
                    end = end + LOG_MAXLENGTH
                } else {
                    Log.i(TAG, message.substring(start, strLength), throwable)
                    break
                }
            }
        }
    }

    @JvmStatic
    fun e(TAG: String, message: String) {
        if (isOpenLog) {
            val strLength = message.length
            var start = 0
            var end = LOG_MAXLENGTH
            for (i in 0..99) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.e(TAG + i, message.substring(start, end))
                    start = end
                    end = end + LOG_MAXLENGTH
                } else {
                    Log.e(TAG, message.substring(start, strLength))
                    break
                }
            }
        }
    }

    @JvmStatic
    fun e(TAG: String, message: String, throwable: Throwable?) {
        if (isOpenLog) {
            val strLength = message.length
            var start = 0
            var end = LOG_MAXLENGTH
            for (i in 0..99) {
                //剩下的文本还是大于规定长度则继续重复截取并输出
                if (strLength > end) {
                    Log.e(TAG + i, message.substring(start, end), throwable)
                    start = end
                    end = end + LOG_MAXLENGTH
                } else {
                    Log.e(TAG, message.substring(start, strLength), throwable)
                    break
                }
            }
        }
    }

}