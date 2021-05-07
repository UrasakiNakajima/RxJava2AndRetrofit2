package com.mobile.common_library.interceptor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.mobile.common_library.BaseApplication;
import com.mobile.common_library.manager.LogManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:55
 * introduce : 接收Cookies拦截器
 */

public class ReceivedCookiesInterceptor implements Interceptor {

    private static final String TAG = "ReceivedCookiesInterceptor";
    private BaseApplication baseApplication;


    public ReceivedCookiesInterceptor(Context context) {
        super();
        baseApplication = (BaseApplication) context.getApplicationContext();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的authorization
        String authorization = originalResponse.header("authorization");
        LogManager.i(TAG, "originalResponse authorization*****" + authorization);
        if (authorization != null && !"".equals(authorization)) {
            baseApplication.setAuthorization(authorization);
            LogManager.i(TAG, "authorization*****" + authorization);
        }
//        //这里获取请求返回的cookie
//        if (!originalResponse.headers("set-cookie").isEmpty()) {
//            String cookie = originalResponse.header("set-cookie").split(";")[0];
//            mineApplication.setCookie(cookie);
//            LogManager.i(TAG, "cookie*****" + cookie);
//        }
        return originalResponse;
    }
}