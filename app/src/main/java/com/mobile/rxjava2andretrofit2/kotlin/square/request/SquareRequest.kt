package com.mobile.rxjava2andretrofit2.kotlin.square.request

import com.mobile.rxjava2andretrofit2.common.ConstantData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface SquareRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET("article/listproject/{currentPage}/json")
    fun getSquareData(
            @Path("currentPage") currentPage: String): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET("article/listproject/{currentPage}/json")
    fun getSquareDetails(
            @Path("currentPage") currentPage: String): Observable<ResponseBody>

}