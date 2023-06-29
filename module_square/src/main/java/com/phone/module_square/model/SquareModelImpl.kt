package com.phone.module_square.model

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.DataSquare
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_square.request.SquareRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

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


}