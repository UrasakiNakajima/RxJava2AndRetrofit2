package com.phone.module_square.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface ISquareModel {

    fun squareData(currentPage: String): Observable<ResponseBody>

    fun squareData2(currentPage: String): Call<ResponseBody>

}