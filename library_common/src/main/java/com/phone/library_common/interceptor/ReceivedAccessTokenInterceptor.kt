package com.phone.library_common.interceptor

import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ReceivedAccessTokenInterceptor : Interceptor {

    private val TAG = ReceivedAccessTokenInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        //这里获取请求返回的accessToken
        val accessToken = originalResponse.header("accessToken")
        accessToken?.let {
            SharedPreferencesManager.put("accessToken", accessToken)
            LogManager.i(TAG, "appToken*****$accessToken")
        }

        //        //这里获取请求返回的cookie
        //        if (!originalResponse.headers("set-cookie").isEmpty()) {
        //            String cookie = originalResponse.header("set-cookie").split(";")[0];
        //            baseApplication.setCookie(cookie);
        //            LogManager.i(TAG, "cookie*****" + cookie);
        //        }
        val builder = originalResponse.newBuilder()
        return builder.build()
    }
}