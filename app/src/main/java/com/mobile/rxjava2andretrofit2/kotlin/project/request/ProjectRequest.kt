package com.mobile.rxjava2andretrofit2.kotlin.project.request

import com.mobile.rxjava2andretrofit2.common.ConstantData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ProjectRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET("article/listproject/{currentPage}/json")
    fun getProjectData(
            @Path("currentPage") currentPage: String): Observable<ResponseBody>

}