package com.mobile.rxjava2andretrofit2.kotlin.mine.request

import com.mobile.rxjava2andretrofit2.common.ConstantData
import com.mobile.rxjava2andretrofit2.common.Url
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface MineRequest {

//    @Headers({"urlname:${ConstantData.TO_FIRST_PAGR_FLAG}"})
    @FormUrlEncoded
    @POST(Url.MINE_URL)
    fun getMineData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @Headers(ConstantData.TO_MINE_FLAG)
    @GET(Url.FIRST_PAGE_DETAILS_URL)
    fun getMineDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

}