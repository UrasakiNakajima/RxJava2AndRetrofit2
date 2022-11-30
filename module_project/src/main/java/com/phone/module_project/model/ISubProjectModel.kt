package com.phone.module_project.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface ISubProjectModel {

    fun subProjectData(
        pageNum: Int,
        tabId: Int
    ): Observable<ResponseBody>

    fun subProjectData2(
        pageNum: Int,
        tabId: Int
    ): Call<ResponseBody>

}