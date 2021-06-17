package com.mobile.common_library.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.mobile.common_library.BaseApplication;
import com.mobile.common_library.manager.LogManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 17:55
 * introduce : 接收Cookies拦截器
 */

public class ReceivedAccessTokenInterceptor implements Interceptor {
	
	private static final String          TAG = "ReceivedCookiesInterceptor";
	private              BaseApplication baseApplication;
	
	public ReceivedAccessTokenInterceptor(Context context) {
		super();
		baseApplication = (BaseApplication) context.getApplicationContext();
	}
	
	@NonNull
	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		
		Response originalResponse = chain.proceed(chain.request());
		//这里获取请求返回的accessToken
		String accessToken = originalResponse.header("accessToken");
		LogManager.i(TAG, "originalResponse accessToken*****" + accessToken);
		if (!TextUtils.isEmpty(accessToken)) {
			baseApplication.setAccessToken(accessToken);
			LogManager.i(TAG, "appToken*****" + accessToken);
		}
		
		//        //这里获取请求返回的cookie
		//        if (!originalResponse.headers("set-cookie").isEmpty()) {
		//            String cookie = originalResponse.header("set-cookie").split(";")[0];
		//            baseApplication.setCookie(cookie);
		//            LogManager.i(TAG, "cookie*****" + cookie);
		//        }
		
		Response.Builder builder = originalResponse.newBuilder();
		return builder.build();
	}
}
