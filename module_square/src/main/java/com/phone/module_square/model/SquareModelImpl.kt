package com.phone.module_square.model

import com.phone.library_common.manager.RetrofitManager
import com.phone.module_square.request.SquareRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class SquareModelImpl() : ISquareModel {

    override fun squareData(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
                .create(SquareRequest::class.java)
                .getSquareData(currentPage)
    }

    override fun squareData2(currentPage: String): Call<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(SquareRequest::class.java)
            .getSquareData2(currentPage)
    }


}