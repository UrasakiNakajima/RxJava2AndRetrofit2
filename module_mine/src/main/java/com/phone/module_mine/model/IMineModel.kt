package com.phone.module_mine.model

import com.phone.library_common.bean.ApiResponse3
import com.phone.library_common.bean.MineResult
import io.reactivex.Observable
import okhttp3.ResponseBody

interface IMineModel {

    suspend fun mineData(bodyParams: Map<String, String>): ApiResponse3<MineResult>

    fun mineData2(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun userData(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun userData(accessToken: String, bodyParams: Map<String, String>): Observable<ResponseBody>


}