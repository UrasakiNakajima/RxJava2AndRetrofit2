package com.phone.module_main.request

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainRequest {

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    fun getMainData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>
}