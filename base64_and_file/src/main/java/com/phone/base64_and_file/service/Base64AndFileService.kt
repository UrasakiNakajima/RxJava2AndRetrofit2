package com.phone.base64_and_file.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class Base64AndFileService : Service() {

    companion object {
        @JvmStatic
        private val TAG = Base64AndFileService::class.java.simpleName
    }

    private var mBinder: Binder? = null

    /**
     * 普通服务的不同之处，onBind()方法不在打酱油，而是会返回一个实例
     *
     * @param intent
     * @return
     */
    override fun onBind(intent: Intent?): IBinder? {
        mBinder = Binder()
        return mBinder
    }

}