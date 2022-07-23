package com.phone.mine_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.mine_module.request.MineRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class MineModelImpl : IMineModel {

    companion object {
        private val TAG = MineModelImpl::class.java.simpleName
    }

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

    override fun userData(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(MineRequest::class.java)
                .getUserData(bodyParams)
    }

    override fun userData(accessToken: String, bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(MineRequest::class.java)
                .getUserData(accessToken, bodyParams)
    }
}