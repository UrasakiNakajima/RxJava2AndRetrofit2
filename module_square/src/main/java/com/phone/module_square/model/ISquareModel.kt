package com.phone.module_square.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import com.phone.library_room.Book
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface ISquareModel {

    suspend fun squareData(currentPage: String): ApiResponse<DataSquare>

    fun squareData2(currentPage: String): Observable<ResponseBody>

    fun downloadFile(): Observable<ResponseBody>

    fun downloadFile2(): Call<ResponseBody>

    fun insertBook(success: String): ApiResponse<Book>

    fun queryBook(): ApiResponse<List<Book>>

}