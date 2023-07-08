package com.phone.library_network.interceptor

import android.text.TextUtils
import com.phone.library_network.manager.RetrofitManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RewriteCacheControlInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val cacheControl = request.cacheControl().toString()
        if (!RetrofitManager.isNetworkAvailable()) {
            request = request.newBuilder()
                .cacheControl(if (TextUtils.isEmpty(cacheControl)) CacheControl.FORCE_NETWORK else CacheControl.FORCE_CACHE)
                .build()
        }
        val originalResponse = chain.proceed(request)
        return if (RetrofitManager.isNetworkAvailable()) {
            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma")
                .build()
        } else {
            originalResponse.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + RetrofitManager.CACHE_STALE_SEC
                )
                .removeHeader("Pragma")
                .build()
        }
    }
}