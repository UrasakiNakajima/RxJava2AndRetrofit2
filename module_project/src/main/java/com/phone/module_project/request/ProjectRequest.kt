package com.phone.module_project.request

import com.phone.common_library.common.ConstantData
import com.phone.common_library.common.ConstantUrl
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
    fun getProjectTabData(): Call<ResponseBody>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SUB_PROJECT_URL)
    fun getSubProjectData(
        @Path("pageNum") pageNum: Int,
        @Query("cid") cid: Int
    ): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
    @GET(ConstantUrl.SUB_PROJECT_URL)
    fun getSubProjectData2(
        @Path("pageNum") pageNum: Int,
        @Query("cid") cid: Int
    ): Call<ResponseBody>


}