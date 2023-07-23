package com.phone.module_main.model

import com.phone.library_network.manager.RetrofitManager.Companion.instance
import com.phone.module_main.request.MainRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 17:10
 * introduce :
 */

class MainModelImpl : IMainModel {

    companion object {
        private val TAG = MainModelImpl::class.java.simpleName
    }

    override fun mainData(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return instance().mRetrofit
            .create(MainRequest::class.java)
            .getMainData(bodyParams)
    }

}
