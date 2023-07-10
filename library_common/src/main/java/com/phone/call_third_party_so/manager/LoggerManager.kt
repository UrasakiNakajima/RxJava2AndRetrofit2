package com.phone.call_third_party_so.manager

import com.orhanobut.logger.Logger

object LoggerManager {

    @JvmStatic
    var DEBUG_ENABLE = false // 是否调试模式

    @JvmStatic
    fun logd(tag: String, message: String) {
        if (DEBUG_ENABLE) {
            Logger.d(tag, message)
        }
    }

    @JvmStatic
    fun logd(message: String) {
        if (DEBUG_ENABLE) {
            Logger.d(message)
        }
    }

    @JvmStatic
    fun loge(throwable: Throwable, message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.e(throwable, message, *args)
        }
    }

    @JvmStatic
    fun loge(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.e(message, *args)
        }
    }

    @JvmStatic
    fun logi(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.i(message, *args)
        }
    }

    @JvmStatic
    fun logv(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.v(message, *args)
        }
    }

    @JvmStatic
    fun logw(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.v(message, *args)
        }
    }

    @JvmStatic
    fun logwtf(message: String, vararg args: Any) {
        if (DEBUG_ENABLE) {
            Logger.wtf(message, *args)
        }
    }

    @JvmStatic
    fun logjson(message: String) {
        if (DEBUG_ENABLE) {
            Logger.json(message)
        }
    }

    @JvmStatic
    fun logxml(message: String) {
        if (DEBUG_ENABLE) {
            Logger.xml(message)
        }
    }

}