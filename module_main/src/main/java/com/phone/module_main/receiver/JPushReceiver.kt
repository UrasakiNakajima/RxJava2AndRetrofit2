package com.phone.module_main.receiver

import android.content.Context
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.phone.library_base.manager.LogManager

class JPushReceiver : JPushMessageReceiver() {

    companion object {
        @JvmStatic
        private val TAG = JPushReceiver::class.java.simpleName
    }

    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        super.onMessage(p0, p1)

        LogManager.i(TAG, "onMessage customMessage*****" + p1.toString());
    }




}