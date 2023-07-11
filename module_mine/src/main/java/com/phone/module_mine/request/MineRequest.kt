package com.phone.module_mine.request

import com.phone.library_network.bean.ApiResponse3
import com.phone.library_common.bean.MineResult
import com.phone.library_base.common.ConstantData
import com.phone.library_base.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface MineRequest {

    @Headers("urlname:${ConstantData.TO_MINE_FLAG}")
    @GET(ConstantUrl.MINE_URL)
    suspend fun getMineData(@QueryMap bodyParams: Map<String, String>): ApiResponse3<MineResult>

    @Headers("urlname:${ConstantData.TO_MINE_FLAG}")
    @GET(ConstantUrl.MINE_URL)
    fun getMineData2(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_MINE_FLAG}")
    @GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
    fun getMineDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    //    @Headers("urlname:${ConstantData.TO_USER_DATA_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.USER_DATA)
    fun getUserData(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @GET(ConstantUrl.USER_DATA)
    fun getUserData(
        @Header("appToken") accessToken: String,
        @QueryMap bodyParams: Map<String, String>
    ): Observable<ResponseBody>


}