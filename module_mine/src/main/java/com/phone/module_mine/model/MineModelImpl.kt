package com.phone.module_mine.model

import com.phone.library_network.bean.ApiResponse3
import com.phone.call_third_party_so.bean.MineResult
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_mine.request.MineRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class MineModelImpl : IMineModel {

    companion object {
        private val TAG = MineModelImpl::class.java.simpleName
    }

    override suspend fun mineData(bodyParams: Map<String, String>): ApiResponse3<MineResult> {
        return RetrofitManager.instance().mRetrofit.create(MineRequest::class.java)
            .getMineData(bodyParams)
    }

    override fun mineData2(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit.create(MineRequest::class.java)
            .getMineData2(bodyParams)
    }

    override fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit.create(MineRequest::class.java)
            .getMineDetails(bodyParams)
    }

    override fun userData(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit.create(MineRequest::class.java)
            .getUserData(bodyParams)
    }

    override fun userData(
        accessToken: String, bodyParams: Map<String, String>
    ): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit.create(MineRequest::class.java)
            .getUserData(accessToken, bodyParams)
    }
}