package com.mobile.common_library.manager;


import android.os.Handler;
import android.os.Looper;

import com.mobile.common_library.callback.OnSubThreadToMainThreadCallback;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/7 15:51
 * introduce :
 */


public class MainThreadManager {

    private Handler mainThreadHandler;
    private OnSubThreadToMainThreadCallback onSubThreadToMainThreadCallback;

    public MainThreadManager() {
    }

    public MainThreadManager(OnSubThreadToMainThreadCallback onSubThreadToMainThreadCallback) {
        this.onSubThreadToMainThreadCallback = onSubThreadToMainThreadCallback;
        if (Looper.myLooper() != Looper.getMainLooper()) {

            // If we finish marking off of the main thread, we need to
            // actually do it on the main thread to ensure correct ordering.
            mainThreadHandler = new Handler(Looper.getMainLooper());
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSubThreadToMainThreadCallback.onSuccess();
                    mainThreadHandler.removeCallbacksAndMessages(null);
                    mainThreadHandler = null;
                }
            });
        } else {
            onSubThreadToMainThreadCallback.onSuccess();
        }
    }

    public void setOnSubThreadToMainThreadCallback(OnSubThreadToMainThreadCallback onSubThreadToMainThreadCallback) {
        this.onSubThreadToMainThreadCallback = onSubThreadToMainThreadCallback;
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
                    onSubThreadToMainThreadCallback.onSuccess();
                    mainThreadHandler.removeCallbacksAndMessages(null);
                    mainThreadHandler = null;
                }
            });
        } else {
            onSubThreadToMainThreadCallback.onSuccess();
        }
    }
}
