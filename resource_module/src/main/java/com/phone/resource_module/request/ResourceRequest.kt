package com.phone.resource_module.request

import com.phone.common_library.common.ConstantData
import com.phone.common_library.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ResourceRequest {

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.RESOURCE_URL + "/{type}/{pageSize}/{currentPage}")
    fun getResourceData(
        @Path("type") type: String,
        @Path("pageSize") pageSize: String,
        @Path("currentPage") currentPage: String
    ): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET("/wxarticle/list/{id}/{pageNum}/json")
    fun getResourceData2(
        @Path("id") tabId: Int,
        @Path("pageNum") pageNum: Int
    ): Call<ResponseBody>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET("/wxarticle/chapters/json")
    fun getResourceTabData(): Call<ResponseBody>


}