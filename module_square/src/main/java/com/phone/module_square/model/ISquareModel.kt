package com.phone.module_square.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import io.reactivex.Observable
import okhttp3.ResponseBody

interface ISquareModel {

    suspend fun squareData(currentPage: String): ApiResponse<DataSquare>

    fun squareData2(currentPage: String): Observable<ResponseBody>

    fun downloadFile(): Observable<ResponseBody>

}