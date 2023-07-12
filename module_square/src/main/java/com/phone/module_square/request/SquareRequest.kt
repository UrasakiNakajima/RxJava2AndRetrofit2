package com.phone.module_square.request

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import com.phone.library_base.common.ConstantData
import com.phone.library_base.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Streaming

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

    /**
     * 下载文件
     *
     */
    @Headers("urlname:" + ConstantData.TO_DOWNLOAD_FILE_FLAG)
    @Streaming
    @GET(ConstantUrl.DOWNLOAD_FILE_URL)
    fun downloadFile(): Observable<ResponseBody>


}