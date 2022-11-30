package com.phone.library_common.manager

import android.os.Handler
import android.os.Looper
import com.phone.library_common.callback.OnCommonSuccessCallback

class MainThreadManager {

    private var mainThreadHandler: Handler? = null
    private var onCommonSuccessCallback: OnCommonSuccessCallback? = null

    constructor() {

    }

    constructor(onCommonSuccessCallback: OnCommonSuccessCallback) {
        if (Looper.myLooper() != Looper.getMainLooper()) {

            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            mainThreadHandler = Handler(Looper.getMainLooper())
            mainThreadHandler!!.post {
                onCommonSuccessCallback.onSuccess()
                mainThreadHandler!!.removeCallbacksAndMessages(null)
                mainThreadHandler = null
            }
        } else {
            onCommonSuccessCallback.onSuccess()
        }
    }

    fun setOnSubThreadToMainThreadCallback(onCommonSuccessCallback: OnCommonSuccessCallback) {
        this.onCommonSuccessCallback = onCommonSuccessCallback
    }

    /**
     * 子线程到主线程
     */
    fun subThreadToUIThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {

            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            mainThreadHandler = Handler(Looper.getMainLooper())
            mainThreadHandler!!.post {
                onCommonSuccessCallback!!.onSuccess()
                mainThreadHandler!!.removeCallbacksAndMessages(null)
                mainThreadHandler = null
            }
        } else {
            onCommonSuccessCallback!!.onSuccess()
        }
    }

}