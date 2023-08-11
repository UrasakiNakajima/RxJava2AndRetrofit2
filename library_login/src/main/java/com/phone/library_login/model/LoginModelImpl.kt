package com.phone.library_login.model

import com.phone.library_network.manager.RetrofitManager
import com.phone.library_login.request.LoginRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

class LoginModelImpl : ILoginModel {

    private val TAG = LoginModelImpl::class.java.simpleName

    override fun getAuthCode(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(LoginRequest::class.java)
            .getAuthCode(bodyParams)
    }

    override fun loginWithAuthCode(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(LoginRequest::class.java)
            .getLoginWithAuthCodeData(bodyParams)
    }

    override fun register(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(LoginRequest::class.java)
            .getRegisterData(bodyParams)
    }

}