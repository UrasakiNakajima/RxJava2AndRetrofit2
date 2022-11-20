package com.phone.square_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface ISquareModel {

    fun squareData(currentPage: String): Observable<ResponseBody>

    fun squareData2(currentPage: String): Call<ResponseBody>

    fun squareDetails(currentPage: String): Observable<ResponseBody>


}