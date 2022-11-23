package com.phone.rxjava2andretrofit2;

import android.text.TextUtils;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.manager.LogManager;

import cn.jpush.android.api.JPushInterface;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 10:07
 * introduce :
 */

public class MineApplication extends BaseApplication {

    private static final String TAG = MineApplication.class.getSimpleName();
    //用户名
    private String userName;
    //用户Id
    private String userId;
    private String date;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    private String registrationId;

    @Override
    public void onCreate() {
        super.onCreate();
        date = getDate();
        if (date == null || "".equals(date)) {
            date = "0";
        }

        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        registrationId = getRegistrationId();
        if (TextUtils.isEmpty(registrationId)) {
            //获取RegistrationID唯一标识
            registrationId = JPushInterface.getRegistrationID(getApplicationContext());
            setRegistrationId(registrationId);
        }
    }

    public static MineApplication getInstance() {
        return (MineApplication) baseApplication;
    }

    public String getUserName() {
        userName = sp.getString("userName", "");
        LogManager.i(TAG, "getUserName***" + userName);
        return userName;
    }

    public void setUserName(String userName) {
        LogManager.i(TAG, "setUserName***" + userName);
        editor.putString("userName", userName);
        editor.commit();
    }

    /**
     * 获取userId
     *
     * @return
     */
    public String getUserId() {
        userId = sp.getString("userId", "");
        LogManager.i(TAG, "getUserId***" + userId);
        return userId;
    }

    /**
     * 存储userId
     *
     * @param userId
     */
    public void setUserId(String userId) {
        LogManager.i(TAG, "setUserId***" + userId);
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getDate() {
        date = sp.getString("date", "");
        return date;
    }

    public void setDate(String date) {
        editor.putString("date", date);
        editor.commit();
    }

    public String getLongitude() {
        longitude = sp.getString("longitude", "");
        return longitude;
    }

    public void setLongitude(String longitude) {
        editor.putString("longitude", longitude);
        editor.commit();
    }

    public String getLatitude() {
        latitude = sp.getString("latitude", "");
        return latitude;
    }

    public void setLatitude(String latitude) {
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public String getRegistrationId() {
        registrationId = sp.getString("registrationId", "");
        LogManager.i(TAG, "getRegistrationId***" + registrationId);
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        LogManager.i(TAG, "setRegistrationId***" + registrationId);
        editor.putString("registrationId", registrationId);
        editor.commit();
    }

    public void setLogout2() {
        LogManager.i(TAG, "setLogout***");
        //        editor.clear();
        editor.remove("userName");
        editor.remove("userId");
        editor.remove("isCopyDatabase");
        editor.remove("date");
        editor.remove("longitude");
        editor.remove("latitude");
        editor.commit();
    }

}
