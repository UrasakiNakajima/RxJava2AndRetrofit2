package com.mobile.mine_module.request

import com.mobile.common_library.common.ConstantData
import com.mobile.common_library.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface MineRequest {

    @Headers("urlname:${ConstantData.TO_MINE_FLAG}")
    @FormUrlEncoded
    @POST(ConstantUrl.MINE_URL)
    fun getMineData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @Headers("urlname:${ConstantData.TO_MINE_FLAG}")
    @GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
    fun getMineDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    //    @Headers("urlname:${ConstantData.TO_USER_DATA_FLAG}")
//    @FormUrlEncoded
    @GET(ConstantUrl.USER_DATA)
    fun getUserData(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @GET(ConstantUrl.USER_DATA)
    fun getUserData(@Header("appToken") token: String, @QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>


}