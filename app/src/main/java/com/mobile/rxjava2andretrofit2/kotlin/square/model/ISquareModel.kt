package com.mobile.rxjava2andretrofit2.kotlin.square.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface ISquareModel {

    fun squareData(currentPage: String): Observable<ResponseBody>

}