package com.mobile.rxjava2andretrofit2.kotlin.resources_show.model

import com.mobile.rxjava2andretrofit2.kotlin.resources_show.model.base.IResourceModel
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.resources_show.request.ResourceRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class ResourceModelImpl : IResourceModel {

    private val TAG = "ResourceModelImpl"

    override fun resourceData(type: String,
                               pageSize: String,
                               currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(ResourceRequest::class.java)
                .getResourceData(type, pageSize, currentPage)
    }

}