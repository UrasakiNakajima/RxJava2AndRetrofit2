package com.phone.library_common.manager

import android.annotation.SuppressLint
import com.phone.library_base.manager.LogManager
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
    @JvmStatic
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
    @JvmStatic
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
    @JvmStatic
    fun dateStrToMillisecond(dateStr: String): Long? {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"))
        //24小时制
        val simpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.parse(dateStr)?.time
    }

    /**
     * String 转 Data
     */
    @JvmStatic
    fun stringConvertDate(time: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        var data: Date? = null
        try {
            data = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return data
    }

    /**
     * 返回发布时间距离当前的时间
     */
    @JvmStatic
    fun timeAgo(createdTime: Date): String? {
        val format = SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
        val agoTimeInMin =
            (Date(System.currentTimeMillis()).time - createdTime.time) / 1000 / 60
        return if (agoTimeInMin <= 1) {
            // 如果在当前时间以前一分钟内
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

    /**
     * 根据时间戳 返回发布时间距离当前的时间
     */
    @JvmStatic
    fun getTimeStampAgo(timeStamp: String): String? {
        val time = timeStamp.toLong()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val string = sdf.format(time * 1000L)
        var date: Date? = null
        try {
            date = sdf.parse(string)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date?.let {
            timeAgo(it)
        }
    }

    @JvmStatic
    fun getCurrentTimeStamp(): String {
        return (System.currentTimeMillis() / 1000).toString()
    }

}