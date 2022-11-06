package com.phone.common_library.manager;


import android.os.Handler;
import android.os.Looper;

import com.phone.common_library.callback.OnCommonSuccessCallback;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:51
 * introduce :
 */


public class MainThreadManager {

    private Handler mainThreadHandler;
    private OnCommonSuccessCallback onCommonSuccessCallback;

    public MainThreadManager() {
    }

    public MainThreadManager(OnCommonSuccessCallback onCommonSuccessCallback) {
        this.onCommonSuccessCallback = onCommonSuccessCallback;
        if (Looper.myLooper() != Looper.getMainLooper()) {

            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            mainThreadHandler = new Handler(Looper.getMainLooper());
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    onCommonSuccessCallback.onSuccess();
                    mainThreadHandler.removeCallbacksAndMessages(null);
                    mainThreadHandler = null;
                }
            });
        } else {
            onCommonSuccessCallback.onSuccess();
        }
    }

    public void setOnSubThreadToMainThreadCallback(OnCommonSuccessCallback onCommonSuccessCallback) {
        this.onCommonSuccessCallback = onCommonSuccessCallback;
    }

    /**
     * 子线程到主线程
     */
    public void subThreadToUIThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {

            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            mainThreadHandler = new Handler(Looper.getMainLooper());
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    onCommonSuccessCallback.onSuccess();
                    mainThreadHandler.removeCallbacksAndMessages(null);
                    mainThreadHandler = null;
                }
            });
        } else {
            onCommonSuccessCallback.onSuccess();
        }
    }

}
