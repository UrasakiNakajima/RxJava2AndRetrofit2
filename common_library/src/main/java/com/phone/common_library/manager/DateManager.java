package com.phone.common_library.manager;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2021/3/10 9:35
 * introduce :
 */

public class DateManager {
    
    private static final String TAG = "DateManager";
    /**
     * 将日期转化成日期字符串
     *
     * @param date
     * @return
     */
    public static String dateToDataStr(Date date) {//可根据需要自行截取数据显示
        Log.i(TAG, "getDate choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    
    /**
     * 将日期转化成时间字符串
     *
     * @param date
     * @return
     */
    public static String dateToTimeStr(Date date) {//可根据需要自行截取数据显示
        Log.i(TAG, "getTime date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }
    
    /**
     * 将时间字符串转化成毫秒数
     *
     * @param dateStr
     * @return
     */
    @SuppressWarnings("JavaDoc")
    public static long dateStrToMillisecond(String dateStr) throws ParseException {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        //24小时制
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long time = simpleDateFormat.parse(dateStr).getTime();
        return time;
    }
    
    /**
     * String 转 Data
     */
    public static Date stringConvertDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date data = null;
        try {
            data = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 返回发布时间距离当前的时间
     */
    public static String timeAgo(Date createdTime) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        if (createdTime != null) {
            long agoTimeInMin = (new Date(System.currentTimeMillis()).getTime() - createdTime.getTime()) / 1000 / 60;
            // 如果在当前时间以前一分钟内
            if (agoTimeInMin <= 1) {
                return "刚刚";
            } else if (agoTimeInMin <= 60) {
                // 如果传入的参数时间在当前时间以前10分钟之内
                return agoTimeInMin + "分钟前";
            } else if (agoTimeInMin <= 60 * 24) {
                return agoTimeInMin / 60 + "小时前";
            } else if (agoTimeInMin <= 60 * 24 * 2) {
                return agoTimeInMin / (60 * 24) + "天前";
            } else {
                return format.format(createdTime);
            }
        } else {
            return format.format(new Date(0));
        }
    }

    /**
     * 根据时间戳 返回发布时间距离当前的时间
     */
    public static String getTimeStampAgo(String timeStamp) {
        Long time = Long.valueOf(timeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String string = sdf.format(time * 1000L);
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeAgo(date);
    }

    public static String getCurrentTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
}
