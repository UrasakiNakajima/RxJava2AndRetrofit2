package com.mobile.rxjava2andretrofit2.interceptor;

import androidx.annotation.NonNull;

import com.mobile.rxjava2andretrofit2.MineApplication;
import com.mobile.rxjava2andretrofit2.manager.LogManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 13:55
 * introduce : 添加Cookies拦截器
 */

public class AddCookiesInterceptor implements Interceptor {

    private static final String TAG = "AddCookiesInterceptor";
    private MineApplication mineApplication;

    public AddCookiesInterceptor(MineApplication mineApplication) {
        super();
        this.mineApplication = mineApplication;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        //添加authorization
        String authorization = mineApplication.getAuthorization();
        if (authorization != null && !"".equals(authorization)) {
            builder.addHeader("authorization", authorization);
            LogManager.i(TAG, "authorization*****" + authorization);
        }
//        //添加cookie
//        String cookie = mineApplication.getCookie();
//        if (cookie != null && !"".equals(cookie)) {
//            builder.addHeader("cookie", cookie);
//            LogManager.i(TAG, "cookie*****" + cookie);
//        }

//        //添加用户代理
//        builder.removeHeader("User-Agent")
//                .addHeader("User-Agent",
//                SystemManager.getUserAgent(mineApplication.getApplicationContext())).build();
        return chain.proceed(builder.build());
    }
}
