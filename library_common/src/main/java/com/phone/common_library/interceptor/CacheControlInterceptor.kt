package com.phone.common_library.interceptor

import com.phone.common_library.manager.RetrofitManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class CacheControlInterceptor:Interceptor {

    private val TAG = "CacheControlInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!RetrofitManager.isNetworkAvailable()) {
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        }
        val originalResponse = chain.proceed(request)
        return if (RetrofitManager.isNetworkAvailable()) {
            // 有网络时 设置缓存为默认值
            val cacheControl = request.cacheControl().toString()
            originalResponse.newBuilder()
                .header("Cache-Control", cacheControl)
                .removeHeader("Pragma") // 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                .build()
        } else {
            // 无网络时 设置超时为1周
            val maxStale = 1000 * 60 * 60 * 24 * 7
            originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
    }
}