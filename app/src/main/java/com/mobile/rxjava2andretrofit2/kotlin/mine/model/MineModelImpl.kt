package com.mobile.rxjava2andretrofit2.kotlin.mine.model

import com.mobile.rxjava2andretrofit2.kotlin.mine.model.base.IMineModel
import com.mobile.rxjava2andretrofit2.kotlin.mine.request.MineRequest
import com.mobile.rxjava2andretrofit2.kotlin.resources.model.base.IResourcesModel
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.request.ResourcesRequest
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