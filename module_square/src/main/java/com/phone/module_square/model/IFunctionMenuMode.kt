package com.phone.module_square.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_room.Book
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface IFunctionMenuMode {

    fun downloadFile(): Observable<ResponseBody>

    fun downloadFile2(): Call<ResponseBody>

    fun insertBook(success: String): ApiResponse<Book>

    fun queryBook(): ApiResponse<List<Book>>

}