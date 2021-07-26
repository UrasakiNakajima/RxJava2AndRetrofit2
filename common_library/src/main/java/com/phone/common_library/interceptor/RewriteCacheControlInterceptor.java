package com.phone.common_library.interceptor;

import android.text.TextUtils;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.manager.RetrofitManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : Urasaki
 * e-mail : 1164688204@qq.com
 * date   : 2021/6/17 9:58
 * desc   :
 * version: 1.0
 */
public class RewriteCacheControlInterceptor implements Interceptor {
	
	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		Request request = chain.request();
		String cacheControl = request.cacheControl().toString();
		if (!RetrofitManager.isNetworkAvailable(BaseApplication.getInstance())) {
			request = request.newBuilder()
						  .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE)
						  .build();
		}
		Response originalResponse = chain.proceed(request);
		if (RetrofitManager.isNetworkAvailable(BaseApplication.getInstance())) {
			//有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
			
			return originalResponse.newBuilder()
					   .header("Cache-Control", cacheControl)
					   .removeHeader("Pragma")
					   .build();
		} else {
			return originalResponse.newBuilder()
					   .header("Cache-Control", "public, only-if-cached, max-stale=" + RetrofitManager.CACHE_STALE_SEC)
					   .removeHeader("Pragma")
					   .build();
		}
	}
}
