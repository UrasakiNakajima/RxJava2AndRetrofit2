package com.phone.module_home.model

import com.phone.library_network.bean.ApiResponse2
import com.phone.call_third_party_so.bean.ResultData
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

interface IHomeModel {

    suspend fun homePage(bodyParams: Map<String, String>): ApiResponse2<ResultData>

    fun homePage2(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun homePageDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

}