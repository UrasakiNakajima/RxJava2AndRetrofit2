package com.phone.module_square.model

import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_room.AppRoomDataBase
import com.phone.library_room.Book
import com.phone.module_square.request.SquareRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class SquareModelImpl() : ISquareModel {

    override suspend fun squareData(currentPage: String): ApiResponse<DataSquare> {
        return RetrofitManager.instance().mRetrofit
            .create(SquareRequest::class.java)
            .getSquareData(currentPage)
    }

    override fun squareData2(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(SquareRequest::class.java)
            .getSquareData2(currentPage)
    }

    override fun downloadFile(): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(SquareRequest::class.java)
            .downloadFile()
    }

    override fun downloadFile2(): Call<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
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
//            Book("書名：${strArr[0]}", "作者：${strArr[1]}", 100, "簡介：${strArr[2]}")
            book.briefIntroduction = "簡介：${strArr[2]}"
        } else {
//            Book("書名：${strArr[0]}", "作者：${strArr[1]}", 100, "")
        }
        appRoomDataBase.bookDao().insert(book)

        val dataEncryptTimes = SharedPreferencesManager.get("dataEncryptTimes", "0")
        if ("0".equals(dataEncryptTimes)) {
            SharedPreferencesManager.put("dataEncryptTimes", "1")
        }

//                val book2 = Book()
//                book2.bookName = "EnglishXC2"
//                book2.anchor = "rommelXC2"
//                appRoomDataBase.bookDao().insert(book2)
//                val bookList = appRoomDataBase.bookDao().queryAll()
//                for (i in 0..bookList.size - 1) {
//                    LogManager.i(TAG, "book*****" + bookList.get(i).bookName)
//                }
//                AppRoomDataBase.decrypt(
//                    AppRoomDataBase.DATABASE_DECRYPT_NAME,
//                    AppRoomDataBase.DATABASE_ENCRYPT_NAME,
//                    AppRoomDataBase.DATABASE_DECRYPT_KEY
//                )
        val apiResponse = ApiResponse<Book>()
        apiResponse.data = book
        return apiResponse
    }

    override fun queryBook(): ApiResponse<List<Book>> {
        val appRoomDataBase = AppRoomDataBase.instance()
        val apiResponse = ApiResponse<List<Book>>()
        apiResponse.data = appRoomDataBase.bookDao().queryAll()
//        AppRoomDataBase.updateInstance()
        return apiResponse
    }


}