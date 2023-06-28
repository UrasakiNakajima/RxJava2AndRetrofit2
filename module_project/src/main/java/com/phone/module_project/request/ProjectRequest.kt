package com.phone.module_project.request

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.ArticleBean
import com.phone.library_common.bean.TabBean
import com.phone.library_common.common.ConstantData
import com.phone.library_common.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.PROJECT_TAB_URL)
    suspend fun getProjectTabData(): ApiResponse<MutableList<TabBean>>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.PROJECT_TAB_URL)
    fun getProjectTabData2(): Call<ResponseBody>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
    @GET(ConstantUrl.SUB_PROJECT_URL)
    suspend fun getSubProjectData(
        @Path("pageNum") pageNum: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ArticleBean>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SUB_PROJECT_URL)
    suspend fun getSubProjectData2(
        @Path("pageNum") pageNum: Int,
        @Query("cid") cid: Int
    ): Call<ResponseBody>


}