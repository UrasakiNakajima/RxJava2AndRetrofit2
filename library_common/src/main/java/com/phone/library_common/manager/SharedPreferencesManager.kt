package com.phone.library_common.manager

import android.content.Context
import android.content.SharedPreferences
import com.phone.library_common.BaseApplication
import com.phone.library_common.BuildConfig
import com.phone.library_common.JavaGetData

object SharedPreferencesManager {

    @JvmStatic
    private val TAG = SharedPreferencesManager::class.java.simpleName

    @JvmStatic
    val MODE = Context.MODE_PRIVATE

    @JvmStatic
    val SHARED_NAME = "shared_app"

    @JvmStatic
    val aesKey = JavaGetData.nativeAesKey(BaseApplication.get(), BuildConfig.IS_RELEASE)

    @JvmStatic
    fun put(key: String, any: Any) {
        val sp = BaseApplication.get().getSharedPreferences(SHARED_NAME, MODE)
        val editor: SharedPreferences.Editor = sp.edit()

        if (any is String) {
//            editor.putString(key, any)
//            if ("address".equals(key)) {
//                LogManager.i(TAG, "put String key*****$key")
//                LogManager.i(TAG, "put String address*****$any")
//            }

            val encryptStr = AesManager.encrypt(any, aesKey)
            if ("address".equals(key)) {
                LogManager.i(TAG, "put String key*****$key")
                LogManager.i(TAG, "put String address*****$encryptStr")
            }
            editor.putString(key, encryptStr)
        } else if (any is Int) {
            editor.putInt(key, any)
        } else if (any is Long) {
            editor.putLong(key, any)
        } else if (any is Float) {
            editor.putFloat(key, any)
        } else if (any is Boolean) {
            editor.putBoolean(key, any)
        }
        editor.apply()
    }

    @JvmStatic
    fun get(key: String, defaultAny: Any): Any {
        val sp = BaseApplication.get().getSharedPreferences(SHARED_NAME, MODE)

        return (if (defaultAny is String) {
            val decryptStr = AesManager.decrypt(sp.getString(key, defaultAny), aesKey)
            if (decryptStr != null) {
                if ("address".equals(key)) {
                    LogManager.i(TAG, "get String key*****$key")
                    LogManager.i(TAG, "get String decryptStr*****$decryptStr")
                }
                decryptStr
            } else {
                val data = sp.getString(key, defaultAny)
                if ("address".equals(key)) {
                    LogManager.i(TAG, "get String data*****$data")
                }
                data
            }
        } else if (defaultAny is Int) {
            sp.getInt(key, defaultAny)
        } else if (defaultAny is Long) {
            sp.getLong(key, defaultAny)
        } else if (defaultAny is Float) {
            sp.getFloat(key, defaultAny)
        } else if (defaultAny is Boolean) {
            sp.getBoolean(key, defaultAny)
        } else {
            defaultAny
        }) as Any
    }

    fun setLogout() {
        val sp = BaseApplication.get().getSharedPreferences(SHARED_NAME, MODE)
        val editor: SharedPreferences.Editor = sp.edit()

        editor.clear()
//        editor.remove("accessToken")
//        editor.remove("isLogin")
        editor.apply()
    }


}