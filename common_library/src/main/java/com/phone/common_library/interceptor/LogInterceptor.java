package com.phone.common_library.interceptor;

import android.content.Context;
import android.webkit.WebSettings;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.manager.LogManager;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LogInterceptor implements Interceptor {
	
	public static String          TAG = "LogInterceptor";
	private       BaseApplication baseApplication;
	
	public LogInterceptor(Context context) {
		super();
		baseApplication = (BaseApplication) context.getApplicationContext();
	}
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		long startTime = System.currentTimeMillis();
		Response response = chain.proceed(chain.request());
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		MediaType mediaType = response.body().contentType();
		String content = response.body().string();
		LogManager.i(TAG, "\n");
		LogManager.i(TAG, "----------Start----------------");
		LogManager.i(TAG, "| " + request.toString());
		String method = request.method();
		if ("POST".equals(method)) {
			StringBuilder stringBuilder = new StringBuilder();
			if (request.body() instanceof FormBody) {
				FormBody body = (FormBody) request.body();
				for (int i = 0; i < body.size(); i++) {
					stringBuilder.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
				}
				stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
				LogManager.i(TAG, "| RequestParams:{" + stringBuilder.toString() + "}");
			}
		}
		LogManager.i(TAG, "| BaseResponse:" + content);
		LogManager.i(TAG, "----------End:" + duration + "毫秒----------");
		
		Response.Builder builder = response.newBuilder();
		builder.removeHeader("User-Agent")//移除旧的
			.addHeader("User-Agent", WebSettings.getDefaultUserAgent(baseApplication));//添加真正的头部
		return builder
				   .body(ResponseBody.create(mediaType, content))
				   .build();
	}
}
