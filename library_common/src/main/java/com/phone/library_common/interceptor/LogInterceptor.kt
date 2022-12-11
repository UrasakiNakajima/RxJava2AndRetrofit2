package com.phone.library_common.interceptor

import android.webkit.WebSettings
import com.phone.library_common.BaseApplication
import com.phone.library_common.manager.LogManager
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class LogInterceptor : Interceptor {

    private var TAG = LogInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        LogManager.i(TAG, "\n")
        LogManager.i(TAG, "----------Start----------------")
        LogManager.i(TAG, "| $request")
        val method = request.method()
        if ("POST" == method) {
            val stringBuilder = StringBuilder()
            if (request.body() is FormBody) {
                val body = request.body() as FormBody?
                body?.let {
                    for (i in 0 until it.size()) {
                        stringBuilder.append(it.encodedName(i) + "=" + it.encodedValue(i) + ",")
                    }
                    stringBuilder.delete(stringBuilder.length - 1, stringBuilder.length)
                    LogManager.i(TAG, "| RequestParams:{$stringBuilder}")
                }
            }
        }
        LogManager.i(TAG, "| BaseResponse:$content")
        LogManager.i(TAG, "----------End:" + duration + "毫秒----------")
        val builder = response.newBuilder()
        builder.removeHeader("User-Agent") //移除旧的
            .addHeader(
                "User-Agent",
                WebSettings.getDefaultUserAgent(BaseApplication.get())
            ) //添加真正的头部
        return builder
            .body(ResponseBody.create(mediaType, content))
            .build()
    }
}