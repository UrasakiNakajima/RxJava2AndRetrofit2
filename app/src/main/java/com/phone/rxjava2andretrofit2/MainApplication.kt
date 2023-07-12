package com.phone.rxjava2andretrofit2

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_base.manager.ThreadPoolManager

class MainApplication : BaseApplication() {

    companion object {
        @JvmStatic
        private val TAG = MainApplication::class.java.simpleName
    }

//    private var userName: String? = null//用户名
//    private var userId: String? = null//用户Id
//    private var date: String? = null
//    private var longitude: String? = null//经度
//    private var latitude: String? = null//纬度
//    private var registrationId: String? = null
//    private var isEncryptionRequired: Boolean = false//用户名

    override fun onCreate() {
        mWhichPage = "Main"
        super.onCreate()


        val processName = getProcessName(this)
        if (processName != null) {
            LogManager.i(TAG, "processName*****$processName")
            if (processName == packageName) {
                //当进程是当前App 的主进程时，才初始化数据
                //初始化com.phone.rxjava2andretrofit2以包名为进程名，项目默认的进程
                initData2()
            }
        }
    }

    private fun initData2() {
        ThreadPoolManager.instance().createScheduledThreadPoolToUIThread(800, {
            SharedPreferencesManager.put("date", "date")
            val date = SharedPreferencesManager.get("date", "") as String
            LogManager.i(TAG, "date*****$date")

            //极光推送初始化
            JPushInterface.setDebugMode(true)
            JPushInterface.init(this)
            var registrationId = SharedPreferencesManager.get("registrationId", "") as String
            LogManager.i(TAG, "initData2 registrationId*****$registrationId")
            if (TextUtils.isEmpty(registrationId)) {
                //获取RegistrationID唯一标识
                registrationId = JPushInterface.getRegistrationID(this@MainApplication)
                SharedPreferencesManager.put("registrationId", registrationId)
            }
        })
    }

//    fun getUserName(): String? {
//        userName = sp.getString("userName", "")
//        LogManager.i(TAG, "getUserName***$userName")
//        return userName
//    }
//
//    fun setUserName(userName: String) {
//        LogManager.i(TAG, "setUserName***$userName")
//        editor.putString("userName", userName)
//        editor.commit()
//    }
//
//    /**
//     * 获取userId
//     *
//     * @return
//     */
//    fun getUserId(): String? {
//        userId = sp.getString("userId", "")
//        LogManager.i(TAG, "getUserId***$userId")
//        return userId
//    }
//
//    /**
//     * 存储userId
//     *
//     * @param userId
//     */
//    fun setUserId(userId: String) {
//        LogManager.i(TAG, "setUserId***$userId")
//        editor.putString("userId", userId)
//        editor.commit()
//    }
//
//    fun getDate(): String? {
//        date = sp.getString("date", "")
//        return date
//    }
//
//    fun setDate(date: String) {
//        editor.putString("date", date)
//        editor.commit()
//    }

//    fun getLongitude(): String? {
//        longitude = sp.getString("longitude", "")
//        return longitude
//    }
//
//    fun setLongitude(longitude: String?) {
//        editor.putString("longitude", longitude)
//        editor.commit()
//    }
//
//    fun getLatitude(): String? {
//        latitude = sp.getString("latitude", "")
//        return latitude
//    }
//
//    fun setLatitude(latitude: String?) {
//        editor.putString("latitude", latitude)
//        editor.commit()
//    }
//
//    fun getRegistrationId(): String? {
//        registrationId = sp.getString("registrationId", "")
//        LogManager.i(TAG, "getRegistrationId***$registrationId")
//        return registrationId
//    }
//
//    fun setRegistrationId(registrationId: String?) {
//        LogManager.i(TAG, "setRegistrationId***$registrationId")
//        editor.putString("registrationId", registrationId)
//        editor.commit()
//    }

//    fun isEncryptionRequired(): Boolean {
//        isEncryptionRequired = sp.getBoolean("isEncryptionRequired", false)
//        LogManager.i(TAG, "isEncryptionRequired***$isEncryptionRequired")
//        return isEncryptionRequired
//    }
//
//    fun setEncryptionRequired(encryptionRequired: Boolean) {
//        LogManager.i(TAG, "setEncryptionRequired***$encryptionRequired")
//        editor.putBoolean("isEncryptionRequired", encryptionRequired)
//        editor.commit()
//    }
//
//    fun setLogout2() {
//        LogManager.i(TAG, "setLogout***")
//        //        editor.clear();
//        editor.remove("userName")
//        editor.remove("userId")
//        editor.remove("isCopyDatabase")
//        editor.remove("date")
//        editor.remove("longitude")
//        editor.remove("latitude")
//        editor.commit()
//    }

}