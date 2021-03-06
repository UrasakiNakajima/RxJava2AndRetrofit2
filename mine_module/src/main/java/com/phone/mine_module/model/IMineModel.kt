package com.phone.mine_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IMineModel {

    fun mineData(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun userData(bodyParams: Map<String, String>): Observable<ResponseBody>

    fun userData(accessToken: String, bodyParams: Map<String, String>): Observable<ResponseBody>


}