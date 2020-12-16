package com.mobile.rxjava2andretrofit2.kotlin.resources.model.base

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IResourcesModel {

    fun resourcesData(): Observable<ResponseBody>

}