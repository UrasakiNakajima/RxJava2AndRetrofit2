package com.phone.common_library.interceptor;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.manager.RetrofitManager;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheControlInterceptor implements Interceptor {
	
	private static final String          TAG = "CacheControlInterceptor";
	private              BaseApplication baseApplication;
	
	public CacheControlInterceptor(BaseApplication mineApplication) {
		super();
		this.baseApplication = mineApplication;
	}
	
	@NonNull
	@Override
	public Response intercept(@NonNull Chain chain) throws IOException {
		Request request = chain.request();
		if (!RetrofitManager.isNetworkAvailable(baseApplication)) {
			request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
		}
		
		Response originalResponse = chain.proceed(request);
		if (RetrofitManager.isNetworkAvailable(baseApplication)) {
			// 有网络时 设置缓存为默认值
			String cacheControl = request.cacheControl().toString();
			return originalResponse.newBuilder()
					   .header("Cache-Control", cacheControl)
					   .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
					   .build();
		} else {
			// 无网络时 设置超时为1周
			int maxStale = 1000 * 60 * 60 * 24 * 7;
			return originalResponse.newBuilder()
					   .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					   .removeHeader("Pragma")
					   .build();
		}
	}
}
