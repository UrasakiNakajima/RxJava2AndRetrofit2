package com.mobile.rxjava2andretrofit2.kotlin.resources_show.model.base

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IResourcesModel {

    fun resourcesData(type: String,
                      pageSize: String,
                      currentPage: String): Observable<ResponseBody>

}