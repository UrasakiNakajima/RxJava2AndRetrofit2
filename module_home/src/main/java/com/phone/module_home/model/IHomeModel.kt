package com.phone.module_home.model

import com.phone.library_network.bean.ApiResponse2
import com.phone.library_common.bean.ResultData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

interface IHomeModel {

    suspend fun homePage(bodyParams: Map<String, String>): ApiResponse2<ResultData>

}