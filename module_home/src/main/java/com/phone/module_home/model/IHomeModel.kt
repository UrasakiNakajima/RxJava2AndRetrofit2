package com.phone.module_home.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

interface IHomeModel {

    fun homePage(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun homePage2(bodyParams: Map<String, String>): Call<ResponseBody>

    fun homePageDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

}