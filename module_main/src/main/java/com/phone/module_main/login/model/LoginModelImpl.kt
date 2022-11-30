package com.phone.module_main.login.model

import com.phone.library_common.manager.RetrofitManager.Companion.get
import com.phone.module_main.login.request.LoginRequest
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
        return get().getRetrofit()
            .create(LoginRequest::class.java)
            .getAuthCode(bodyParams)
    }

    override fun loginWithAuthCode(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return get().getRetrofit()
            .create(LoginRequest::class.java)
            .getLoginWithAuthCodeData(bodyParams)
    }

    override fun register(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return get().getRetrofit()
            .create(LoginRequest::class.java)
            .getRegisterData(bodyParams)
    }

}