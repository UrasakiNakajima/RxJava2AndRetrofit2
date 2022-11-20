package com.phone.resource_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface IResourceChildModel {

    fun resourceData(type: String,
                      pageSize: String,
                      currentPage: String): Observable<ResponseBody>

    fun resourceData2(tabId: Int,
                      pageNum: Int): Call<ResponseBody>

}