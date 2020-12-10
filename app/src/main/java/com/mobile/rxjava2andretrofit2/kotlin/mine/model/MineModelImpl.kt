package com.mobile.rxjava2andretrofit2.kotlin.mine.model.base

import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.request.MineRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class MineModelImpl : IMineModel {

    private val TAG = "MineModelImpl"

    override fun mineData(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(MineRequest::class.java)
                .getMineData(bodyParams)
    }

    override fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(MineRequest::class.java)
                .getMineDetails(bodyParams)
    }
}