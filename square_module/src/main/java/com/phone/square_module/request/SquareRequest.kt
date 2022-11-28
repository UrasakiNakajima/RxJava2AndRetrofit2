package com.phone.square_module.request

import com.phone.common_library.common.ConstantData
import com.phone.common_library.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface SquareRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SQUARE_URL)
    fun getSquareData(
        @Path("currentPage") currentPage: String
    ): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SQUARE_URL)
    fun getSquareData2(
        @Path("currentPage") currentPage: String
    ): Call<ResponseBody>

}