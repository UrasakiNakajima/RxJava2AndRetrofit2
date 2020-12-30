package com.mobile.rxjava2andretrofit2.kotlin.resource.request

import com.mobile.rxjava2andretrofit2.common.ConstantData
import com.mobile.rxjava2andretrofit2.common.Url
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ResourceRequest {

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
//    @FormUrlEncoded
    @GET(Url.RESOURCE_URL + "{type}/{pageSize}/{currentPage}")
    fun getResourceData(
            @Path("type") type: String,
            @Path("pageSize") pageSize: String,
            @Path("currentPage") currentPage: String): Observable<ResponseBody>

}