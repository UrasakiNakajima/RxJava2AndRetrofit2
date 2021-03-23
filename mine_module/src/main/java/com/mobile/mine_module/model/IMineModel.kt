package com.mobile.mine_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IMineModel {

    fun mineDetails(bodyParams: Map<String, String>): Observable<ResponseBody>

}