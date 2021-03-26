package com.mobile.resource_module.model.base

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IResourceModel {

    fun resourceData(type: String,
                      pageSize: String,
                      currentPage: String): Observable<ResponseBody>

}