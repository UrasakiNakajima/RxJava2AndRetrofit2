package com.mobile.rxjava2andretrofit2.kotlin.mine.model.base

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IMineModel {

    fun mineData(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

}