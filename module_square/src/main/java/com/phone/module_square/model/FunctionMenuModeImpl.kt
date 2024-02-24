package com.phone.module_square.model

import com.phone.library_base.manager.LogManager
import com.phone.library_network.bean.ApiResponse
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_room.AppRoomDataBase
import com.phone.library_room.Book
import com.phone.module_square.request.SquareRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class FunctionMenuModeImpl : IFunctionMenuMode {

    companion object {
        private val TAG = FunctionMenuModeImpl::class.java.simpleName
    }

    override fun downloadFile(): Observable<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(SquareRequest::class.java)
            .downloadFile()
    }

    override fun downloadFile2(): Call<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(SquareRequest::class.java)
            .downloadFile2()
    }

    override fun insertBook(success: String): ApiResponse<Book> {
        val appRoomDataBase = AppRoomDataBase.instance()
        val strArr = success.split(".")
        val book = Book()
        book.bookName = "書名：${strArr[0]}"
        book.anchor = "作者：${strArr[1]}"
        if (strArr.size > 2) {
            book.briefIntroduction = "簡介：${strArr[2]}"
        }
        appRoomDataBase.bookDao().insert(book)

        val apiResponse = ApiResponse<Book>()
        apiResponse.data = book
        return apiResponse
    }

    override fun queryBook(): ApiResponse<List<Book>> {
        val appRoomDataBase = AppRoomDataBase.instance()
        val apiResponse = ApiResponse<List<Book>>()
        apiResponse.data = appRoomDataBase.bookDao().queryAll()
        LogManager.i(TAG, "queryBook*****${apiResponse.data.toString()}")
        return apiResponse
    }


}