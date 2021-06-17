package com.mobile.common_library.interceptor;

import com.mobile.common_library.manager.RetrofitManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/6/17 10:02
 * desc   :
 * version: 1.0
 */
public class HeaderInterceptor implements Interceptor {
	
	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		Request originalRequest = chain.request();
		Request.Builder builder = originalRequest.newBuilder();
		//缓存
		builder.header("Cache-Control", RetrofitManager.getCacheControl());
		//设置具体的header内容
		//                long time = System.currentTimeMillis();
		//                builder.header("timestamp", time + "");
		//设置电影模拟请求头
		long time = System.currentTimeMillis();
		builder.header("timestamp", time + "");
		builder.header("version", "1.1.0");
		builder.header("imei", "861875048330495");
		builder.header("platform", "android");
		builder.header("sign", RetrofitManager.buildSign("CPudpbCP20JnOQCmZ7UXHS5uGKvf64S6", time));
		builder.header("channel", "flyme");
		builder.header("device", "Mi A2");
		builder.header("token", "");
		builder.header("Accept", "application/json");
		
		//                timestamp:
		//                 version: 1.1.0
		//                 imei: 861875048330495
		//                 platform: android
		//                 sign: cd428b6f74eb58374ac395e82f2b2ebf
		//                 channel: flyme
		//                 device: Mi A2
		//                 token:
		//                 Accept: application/json
		
		Request.Builder requestBuilder =
			builder.method(originalRequest.method(), originalRequest.body());
		Request request = requestBuilder.build();
		return chain.proceed(request);
	}
}
