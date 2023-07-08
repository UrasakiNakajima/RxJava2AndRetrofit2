package com.phone.module_resource.request

import com.phone.library_network.bean.ApiResponse
import com.phone.library_custom_view.bean.ArticleBean
import com.phone.library_common.bean.TabBean
import com.phone.library_common.common.ConstantData
import com.phone.library_common.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ResourceRequest {

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET(ConstantUrl.RESOURCE_TAB_URL)
    suspend fun getResourceTabData(): ApiResponse<MutableList<TabBean>>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET(ConstantUrl.RESOURCE_TAB_URL)
    suspend fun getResourceTabData2(): Call<ResponseBody>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
    @GET(ConstantUrl.SUB_RESOURCE_URL)
    suspend fun getSubResourceData(
        @Path("id") id: Int,
        @Path("pageNum") pageNum: Int
    ): ApiResponse<ArticleBean>

    @Headers("urlname:${ConstantData.TO_RESOURCE_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SUB_RESOURCE_URL)
    suspend fun getSubResourceData2(
        @Path("id") id: Int,
        @Path("pageNum") pageNum: Int
    ): Observable<ResponseBody>


}