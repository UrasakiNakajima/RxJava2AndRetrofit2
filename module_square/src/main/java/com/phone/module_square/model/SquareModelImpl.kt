package com.phone.module_square.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import com.phone.library_network.manager.RetrofitManager
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


}