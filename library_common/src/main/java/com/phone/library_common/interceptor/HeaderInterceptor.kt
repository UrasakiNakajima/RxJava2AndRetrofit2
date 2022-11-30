package com.phone.library_common.interceptor

import com.phone.library_common.manager.RetrofitManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        //缓存
        builder.header("Cache-Control", RetrofitManager.getCacheControl())
        //设置具体的header内容
        //                long time = System.currentTimeMillis();
        //                builder.header("timestamp", time + "");
        //设置电影模拟请求头
        val time = System.currentTimeMillis()
        builder.header("timestamp", time.toString() + "")
        builder.header("version", "1.1.0")
        builder.header("imei", "861875048330495")
        builder.header("platform", "android")
        builder.header("sign", RetrofitManager.buildSign("CPudpbCP20JnOQCmZ7UXHS5uGKvf64S6", time))
        builder.header("channel", "flyme")
        builder.header("device", "Mi A2")
        builder.header("token", "")
        builder.header("Accept", "application/json")

        //                timestamp:
        //                 version: 1.1.0
        //                 imei: 861875048330495
        //                 platform: android
        //                 sign: cd428b6f74eb58374ac395e82f2b2ebf
        //                 channel: flyme
        //                 device: Mi A2
        //                 token:
        //                 Accept: application/json
        val requestBuilder = builder.method(originalRequest.method(), originalRequest.body())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}