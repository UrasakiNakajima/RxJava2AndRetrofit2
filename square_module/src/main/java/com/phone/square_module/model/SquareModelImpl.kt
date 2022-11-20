package com.phone.square_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.square_module.request.SquareRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class SquareModelImpl() : ISquareModel {

    override fun squareData(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(SquareRequest::class.java)
                .getSquareData(currentPage)
    }

    override fun squareData2(currentPage: String): Call<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(SquareRequest::class.java)
            .getSquareData2(currentPage)
    }

    override fun squareDetails(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(SquareRequest::class.java)
                .getSquareDetails(currentPage)
    }


}