package com.phone.module_main.model

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 17:03
 * introduce :
 */

interface IMainModel {

    fun mainData(bodyParams: Map<String, String>): Observable<ResponseBody>
}