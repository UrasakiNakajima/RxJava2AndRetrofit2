package com.phone.common_library.interceptor

import com.phone.common_library.common.ConstantData
import com.phone.common_library.manager.LogManager
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BaseUrlManagerInterceptor : Interceptor {

    private val TAG = BaseUrlManagerInterceptor::class.java.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始的originalRequest
        var originalRequest = chain.request()
        //获取originalRequest的创建者builder
        val builder = originalRequest.newBuilder()
        //获取当前的url
        val oldUrl = originalRequest.url()
        LogManager.i(TAG, "intercept:------------oldUrl---------->$oldUrl")

        //获取头信息的集合如：jeapp ,njeapp ,mall
        val urlnameList = originalRequest.headers("urlname")
        return if (urlnameList.size > 0) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader("urlname")
            //获取头信息中配置的value,如：manage或者mdffx
            val urlname = urlnameList[0]
            LogManager.i(TAG, "intercept:-------urlname------$urlname")
            var baseURL: HttpUrl? = null
            //根据头信息中配置的value,来匹配新的base_url地址
            urlname?.let {
                when (it) {
                    ConstantData.TO_FIRST_PAGR_FLAG -> baseURL =
                        HttpUrl.parse(ConstantData.TO_FIRST_PAGR_URL)
                    ConstantData.TO_PROJECT_FLAG -> baseURL =
                        HttpUrl.parse(ConstantData.TO_PROJECT_URL)
                    ConstantData.TO_RESOURCE_FLAG -> baseURL =
                        HttpUrl.parse(ConstantData.TO_RESOURCE_URL)
                    ConstantData.TO_MINE_FLAG -> baseURL = HttpUrl.parse(ConstantData.TO_MINE_URL)
                    ConstantData.TO_USER_DATA_FLAG -> baseURL =
                        HttpUrl.parse(ConstantData.TO_USER_DATA_URL)
                    else -> {}
                }
                //				if ("jeapp".equals(urlname)) {
                //					baseURL = HttpUrl.parse(ConstantData.PRE_URL);
                //				} else if ("njeapp".equals(urlname)) {
                //					baseURL = HttpUrl.parse(ConstantData.PRE_NEW_URL);
                //				} else if ("mall".equals(urlname)) {
                //					baseURL = HttpUrl.parse(ConstantData.MALL_URL);
                //				}
                LogManager.i(TAG, "intercept:-----oldUrl-----$oldUrl")

                baseURL?.let {
                    //重建新的HttpUrl，需要重新设置的url部分
                    val newHttpUrl = oldUrl.newBuilder()
                        .scheme(it.scheme()) //http协议如：http或者https
                        .host(it.host()) //主机地址
                        .port(it.port()) //端口
                        .build()
                    LogManager.i(TAG, "intercept:------scheme----" + it.scheme())
                    LogManager.i(TAG, "intercept:-----host-----" + it.host())
                    LogManager.i(TAG, "intercept:-----port-----" + it.port())
                    LogManager.i(TAG, "intercept:-----newHttpUrl-----$newHttpUrl")

                    //获取处理后的新newRequest
                    val newBuilder = builder.url(newHttpUrl)
                    val newRequest = newBuilder.build()
                    return chain.proceed(newRequest)
                }
            }
            originalRequest = builder.build()
            chain.proceed(originalRequest)
        } else {
            originalRequest = builder.build()
            chain.proceed(originalRequest)
        }
    }

}