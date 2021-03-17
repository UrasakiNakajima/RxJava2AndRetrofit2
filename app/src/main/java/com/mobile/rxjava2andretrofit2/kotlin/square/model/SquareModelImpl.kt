package com.mobile.rxjava2andretrofit2.kotlin.square.model

import com.mobile.rxjava2andretrofit2.kotlin.square.request.SquareRequest
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import io.reactivex.Observable
import okhttp3.ResponseBody

class SquareModelImpl() : ISquareModel {

    override fun squareData(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(SquareRequest::class.java)
                .getSquareData(currentPage)
    }

    override fun squareDetails(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(SquareRequest::class.java)
                .getSquareDetails(currentPage)
    }


}