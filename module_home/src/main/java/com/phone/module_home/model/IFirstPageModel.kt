package com.phone.module_home.model

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

interface IFirstPageModel {

    fun firstPage(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun firstPageDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

}