package com.mobile.rxjava2andretrofit2.kotlin.mine.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IMineModel {

    fun mineData(bodyParams: Map<String, String>): Observable<ResponseBody>

}