package com.phone.module_main.service

import cn.jpush.android.service.JCommonService

class JPushService : JCommonService() {

    companion object {
        @JvmStatic
        private val TAG = JPushService::class.java.simpleName
    }

}