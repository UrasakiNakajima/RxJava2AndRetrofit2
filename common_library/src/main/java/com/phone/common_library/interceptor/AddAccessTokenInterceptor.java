package com.phone.common_library.interceptor;

import android.text.TextUtils;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.manager.LogManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 13:55
 * introduce : 添加Cookies拦截器
 */

public class AddAccessTokenInterceptor implements Interceptor {
	
	private static final String          TAG = "AddCookiesInterceptor";
	private              BaseApplication baseApplication;
	
	public AddAccessTokenInterceptor(BaseApplication baseApplication) {
		super();
		this.baseApplication = baseApplication;
	}
	
	@NonNull
	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request.Builder builder = chain.request().newBuilder();
		
		//添加accessToken
		String accessToken = baseApplication.getAccessToken();
		if (!TextUtils.isEmpty(accessToken)) {
			builder.addHeader("accessToken", accessToken);
			LogManager.i(TAG, "accessToken*****" + accessToken);
		}
		
		//        //添加cookie
		//        String cookie = baseApplication.getCookie();
		//        if (cookie != null && !"".equals(cookie)) {
		//            builder.addHeader("cookie", cookie);
		//            LogManager.i(TAG, "cookie*****" + cookie);
		//        }
		
		//        //添加用户代理
		//        builder.removeHeader("User-Agent")
		//                .addHeader("User-Agent",
		//                SystemManager.getUserAgent(baseApplication.getApplicationContext())).build();
		
		//		builder.removeHeader("User-Agent")//移除旧的
		//			.addHeader("User-Agent", WebSettings.getDefaultUserAgent(baseApplication));//添加真正的头部
		
		return chain.proceed(builder.build());
	}
}
