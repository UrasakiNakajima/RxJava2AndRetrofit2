package com.phone.common_library.manager

import android.util.Log

object LogManager {

    private val isOpenLog = true

    //规定每段显示的长度
    private val LOG_MAXLENGTH = 2000

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