package com.phone.rxjava2andretrofit2.receiver

import cn.jpush.android.service.JPushMessageReceiver

class JiGuangPushReceiver : JPushMessageReceiver() {

    companion object {
        @JvmStatic
        private val TAG = JiGuangPushReceiver::class.java.simpleName
    }

}