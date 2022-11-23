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
    @GET(ConstantUrl.RESOURCE_TAB_URL)
    fun getResourceTabData(): Call<ResponseBody>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SUB_RESOURCE_URL)
    fun getSubResourceData(
        @Path("id") id: Int,
        @Path("pageNum") pageNum: Int
    ): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET(ConstantUrl.SUB_RESOURCE_URL)
    fun getSubResourceData2(
        @Path("id") id: Int,
        @Path("pageNum") pageNum: Int
    ): Call<ResponseBody>


}