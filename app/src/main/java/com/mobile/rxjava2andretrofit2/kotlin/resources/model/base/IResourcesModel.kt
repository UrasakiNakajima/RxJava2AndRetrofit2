package com.mobile.rxjava2andretrofit2.kotlin.resources.model.base

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Path

interface IResourcesModel {

    fun resourcesData(type: String,
                      pageSize: String,
                      currentPage: String): Observable<ResponseBody>

}