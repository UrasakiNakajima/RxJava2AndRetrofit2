package com.phone.base64_and_file.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.phone.common_library.callback.OnCommonSingleParamCallback;

public class Base64AndFileServiceConnection implements ServiceConnection {

    private static final String TAG = Base64AndFileServiceConnection.class.getSimpleName();
    private Binder binder;
    private OnCommonSingleParamCallback<Binder> onCommonSingleParamCallback;

    public Base64AndFileServiceConnection(OnCommonSingleParamCallback<Binder> onCommonSingleParamCallback) {
        this.onCommonSingleParamCallback = onCommonSingleParamCallback;
    }

    /**
     * 可交互的后台服务与普通服务的不同之处，就在于这个connection建立起了两者的联系
     *
     * @param name
     */
    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    /**
     * onServiceConnected()方法关键，在这里实现对服务的方法的调用
     *
     * @param name
     * @param service
     */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (Binder) service;
        onCommonSingleParamCallback.onSuccess(binder);
    }

}
