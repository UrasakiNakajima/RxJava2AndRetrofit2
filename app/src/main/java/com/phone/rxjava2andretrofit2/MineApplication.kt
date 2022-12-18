package com.phone.rxjava2andretrofit2

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ThreadPoolManager

class MineApplication : BaseApplication() {

    companion object {
        @JvmStatic
        private val TAG = MineApplication::class.java.simpleName
    }

    private var userName: String? = null//用户名
    private var userId: String? = null//用户Id
    private var date: String? = null
    private var longitude: String? = null//经度
    private var latitude: String? = null//纬度
    private var registrationId: String? = null

    override fun onCreate() {
        super.onCreate()

        ThreadPoolManager.get().createScheduledThreadPoolToUIThread(500, {
            date = getDate()
            if (date == null || "" == date) {
                date = "0"
            }

            //极光推送初始化
            JPushInterface.setDebugMode(true)
            JPushInterface.init(this)
            registrationId = getRegistrationId()
            if (TextUtils.isEmpty(registrationId)) {
                //获取RegistrationID唯一标识
                registrationId = JPushInterface.getRegistrationID(applicationContext)
                setRegistrationId(registrationId)
            }
        })
    }

    fun getUserName(): String? {
        userName = sp.getString("userName", "")
        LogManager.i(TAG, "getUserName***$userName")
        return userName
    }

    fun setUserName(userName: String) {
        LogManager.i(TAG, "setUserName***$userName")
        editor.putString("userName", userName)
        editor.commit()
    }

    /**
     * 获取userId
     *
     * @return
     */
    fun getUserId(): String? {
        userId = sp.getString("userId", "")
        LogManager.i(TAG, "getUserId***$userId")
        return userId
    }

    /**
     * 存储userId
     *
     * @param userId
     */
    fun setUserId(userId: String) {
        LogManager.i(TAG, "setUserId***$userId")
        editor.putString("userId", userId)
        editor.commit()
    }

    fun getDate(): String? {
        date = sp.getString("date", "")
        return date
    }

    fun setDate(date: String?) {
        editor.putString("date", date)
        editor.commit()
    }

    fun getLongitude(): String? {
        longitude = sp.getString("longitude", "")
        return longitude
    }

    fun setLongitude(longitude: String?) {
        editor.putString("longitude", longitude)
        editor.commit()
    }

    fun getLatitude(): String? {
        latitude = sp.getString("latitude", "")
        return latitude
    }

    fun setLatitude(latitude: String?) {
        editor.putString("latitude", latitude)
        editor.commit()
    }

    fun getRegistrationId(): String? {
        registrationId = sp.getString("registrationId", "")
        LogManager.i(TAG, "getRegistrationId***$registrationId")
        return registrationId
    }

    fun setRegistrationId(registrationId: String?) {
        LogManager.i(TAG, "setRegistrationId***$registrationId")
        editor.putString("registrationId", registrationId)
        editor.commit()
    }

    fun setLogout2() {
        LogManager.i(TAG, "setLogout***")
        //        editor.clear();
        editor.remove("userName")
        editor.remove("userId")
        editor.remove("isCopyDatabase")
        editor.remove("date")
        editor.remove("longitude")
        editor.remove("latitude")
        editor.commit()
    }

}