package com.phone.module_square.request

import com.phone.library_network.bean.ApiResponse
import com.phone.call_third_party_so.bean.DataSquare
import com.phone.library_base.common.ConstantData
import com.phone.library_base.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface SquareRequest {

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SQUARE_URL)
    suspend fun getSquareData(
        @Path("currentPage") currentPage: String
    ): ApiResponse<DataSquare>

    @Headers("urlname:${ConstantData.TO_PROJECT_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.SQUARE_URL)
    fun getSquareData2(
        @Path("currentPage") currentPage: String
    ): Observable<ResponseBody>

}