package com.mobile.rxjava2andretrofit2.java.interceptor;

import androidx.annotation.NonNull;

import com.mobile.rxjava2andretrofit2.java.MineApplication;
import com.mobile.rxjava2andretrofit2.java.first_page.manager.NetWorkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheControlInterceptor implements Interceptor {

    private static final String TAG = "CacheControlInterceptor";
    private MineApplication mineApplication;

    public CacheControlInterceptor(MineApplication mineApplication) {
        super();
        this.mineApplication = mineApplication;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetworkConnected(mineApplication)) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response originalResponse = chain.proceed(request);
        if (NetWorkUtil.isNetworkConnected(mineApplication)) {
            // 有网络时 设置缓存为默认值
            String cacheControl = request.cacheControl().toString();
            return originalResponse.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            // 无网络时 设置超时为1周
            int maxStale = 60 * 60 * 24 * 7;
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
