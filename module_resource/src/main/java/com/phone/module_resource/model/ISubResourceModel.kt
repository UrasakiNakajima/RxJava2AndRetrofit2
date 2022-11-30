package com.phone.module_resource.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface ISubResourceModel {

    fun subResourceData(
        tabId: Int,
        pageNum: Int
    ): Observable<ResponseBody>

    fun subResourceData2(
        tabId: Int,
        pageNum: Int
    ): Call<ResponseBody>

}