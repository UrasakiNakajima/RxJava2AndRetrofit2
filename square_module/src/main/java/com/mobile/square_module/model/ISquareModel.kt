package com.mobile.square_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface ISquareModel {

    fun squareData(currentPage: String): Observable<ResponseBody>

    fun squareDetails(currentPage: String): Observable<ResponseBody>


}