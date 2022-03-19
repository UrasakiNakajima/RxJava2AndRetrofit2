package com.phone.base64_and_file.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Base64AndFileService extends Service {

    private static final String TAG = "Base64AndFileService";
    private Binder mBinder;
    /**
     * 普通服务的不同之处，onBind()方法不在打酱油，而是会返回一个实例
     *
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new Binder();
        return mBinder;
    }

}
