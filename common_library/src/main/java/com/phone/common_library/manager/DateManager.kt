package com.phone.common_library.manager

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2021/3/10 9:35
 * introduce :
 */

object DateManager {

    private val TAG = DateManager::class.java.simpleName

    /**
     * 将日期转化成日期字符串
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun dateToDataStr(date: Date): String? { //可根据需要自行截取数据显示
        LogManager.i(TAG, "getDate choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    /**
     * 将日期转化成时间字符串
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun dateToTimeStr(date: Date): String? { //可根据需要自行截取数据显示
        LogManager.i(TAG, "getTime date millis: " + date.time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    /**
     * 将时间字符串转化成毫秒数
     *
     * @param dateStr
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun dateStrToMillisecond(dateStr: String?): Long? {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"))
        //24小时制
        val simpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        return dateStr?.let {
            simpleDateFormat.parse(dateStr)?.time
        }
    }

    /**
     * String 转 Data
     */
    fun stringConvertDate(time: String?): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        var data: Date? = null
        try {
            time?.let {
                data = sdf.parse(time)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return data
    }

    /**
     * 返回发布时间距离当前的时间
     */
    fun timeAgo(createdTime: Date?): String? {
        val format = SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
        return createdTime?.let {
            val agoTimeInMin =
                (Date(System.currentTimeMillis()).time - createdTime.time) / 1000 / 60
            // 如果在当前时间以前一分钟内
            if (agoTimeInMin <= 1) {
                "刚刚"
            } else if (agoTimeInMin <= 60) {
                // 如果传入的参数时间在当前时间以前10分钟之内
                agoTimeInMin.toString() + "分钟前"
            } else if (agoTimeInMin <= 60 * 24) {
                agoTimeInMin.div(60).toString() + "小时前"
            } else if (agoTimeInMin <= 60 * 24 * 2) {
                agoTimeInMin.div(60 * 24).toString() + "天前"
            } else {
                format.format(createdTime)
            }
        }
    }

    /**
     * 根据时间戳 返回发布时间距离当前的时间
     */
    fun getTimeStampAgo(timeStamp: String?): String? {
        val time = timeStamp?.toLong()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

        return time?.let {
            val string = sdf.format(time * 1000L)
            var date: Date? = null
            try {
                date = sdf.parse(string)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            timeAgo(date)
        }
    }

    fun getCurrentTimeStamp(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }
}