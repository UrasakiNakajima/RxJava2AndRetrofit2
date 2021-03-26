package com.mobile.resource_module.request

import com.mobile.common_library.common.ConstantData
import com.mobile.common_library.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ResourceRequest {

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.RESOURCE_URL + "{type}/{pageSize}/{currentPage}")
    fun getResourceData(
            @Path("type") type: String,
            @Path("pageSize") pageSize: String,
            @Path("currentPage") currentPage: String): Observable<ResponseBody>

}