package com.mobile.project_module.request

import com.mobile.common_library.common.ConstantData
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