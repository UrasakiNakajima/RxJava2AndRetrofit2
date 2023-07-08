package com.phone.library_base.manager

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.phone.library_base.BaseApplication

object SharedPreferencesManager {

    @JvmStatic
    private val TAG = SharedPreferencesManager::class.java.simpleName

    @JvmStatic
    val MODE = Context.MODE_PRIVATE

    @JvmStatic
    val SHARED_NAME = "shared_app"

    @JvmStatic
    fun put(key: String, defaultAny: Any) {
        val sp = BaseApplication.instance().getSharedPreferences(SHARED_NAME, MODE)
        val editor: SharedPreferences.Editor = sp.edit()

        if (defaultAny is String) {
            editor.putString(key, defaultAny)
        } else if (defaultAny is Int) {
            editor.putInt(key, defaultAny)
        } else if (defaultAny is Long) {
            editor.putLong(key, defaultAny)
        } else if (defaultAny is Float) {
            editor.putFloat(key, defaultAny)
        } else if (defaultAny is Boolean) {
            editor.putBoolean(key, defaultAny)
        }
        editor.apply()
    }

    @JvmStatic
    fun putDecryptStr(key: String, aesKey: String, str: String) {
        val sp = BaseApplication.instance().getSharedPreferences(SHARED_NAME, MODE)
        val editor: SharedPreferences.Editor = sp.edit()
        val encryptStr = AesManager.encrypt(str, aesKey)
        editor.putString(key, encryptStr)
        editor.apply()
    }

    @JvmStatic
    fun get(key: String, defaultAny: Any): Any {
        val sp = BaseApplication.instance().getSharedPreferences(SHARED_NAME, MODE)

        return (if (defaultAny is String) {
            sp.getString(key, defaultAny)
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

    @JvmStatic
    fun getDecryptStr(key: String, aesKey: String, defaultStr: String): String {
        val sp = BaseApplication.instance().getSharedPreferences(SHARED_NAME, MODE)
        val decryptStr = AesManager.decrypt(sp.getString(key, defaultStr), aesKey)
        return if (!TextUtils.isEmpty(decryptStr)) {
            decryptStr
        } else {
            sp.getString(key, defaultStr)!!
        }
    }

    fun setLogout() {
        val sp = BaseApplication.instance().getSharedPreferences(SHARED_NAME, MODE)
        val editor: SharedPreferences.Editor = sp.edit()

        editor.clear()
//        editor.remove("accessToken")
//        editor.remove("isLogin")
        editor.apply()
    }


}