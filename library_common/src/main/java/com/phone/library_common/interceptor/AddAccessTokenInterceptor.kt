package com.phone.library_common.interceptor

import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddAccessTokenInterceptor : Interceptor {

    private val TAG = AddAccessTokenInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        //添加accessToken
        val accessToken = SharedPreferencesManager.get("accessToken", "") as String
        accessToken.let {
            builder.addHeader("accessToken", accessToken)
            LogManager.i(TAG, "accessToken*****$accessToken")
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
        return chain.proceed(builder.build())
    }
}