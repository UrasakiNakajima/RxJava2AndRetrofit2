package com.phone.main_module.login.model

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

interface ILoginModel {

    fun getAuthCode(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun loginWithAuthCode(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun register(bodyParams: Map<String, String>): Observable<ResponseBody>

}