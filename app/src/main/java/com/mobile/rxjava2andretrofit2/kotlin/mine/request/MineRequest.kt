package com.mobile.rxjava2andretrofit2.kotlin.mine.request

import com.mobile.rxjava2andretrofit2.java.common.Url
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface MineRequest {

    @FormUrlEncoded
    @POST(Url.MINE_URL)
    fun getMineData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @GET(Url.FIRST_PAGE_DETAILS_URL)
    fun getMineDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    fun getFeedbackResult(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

}