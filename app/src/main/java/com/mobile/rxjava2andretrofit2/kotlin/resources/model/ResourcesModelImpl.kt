package com.mobile.rxjava2andretrofit2.kotlin.resources.model

import com.mobile.rxjava2andretrofit2.kotlin.resources.model.base.IResourcesModel
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.resources.request.ResourcesRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class ResourcesModelImpl : IResourcesModel {

    private val TAG = "ResourcesModelImpl"

    override fun resourcesData(type: String,
                               pageSize: String,
                               currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(ResourcesRequest::class.java)
                .getResourcesData(type, pageSize, currentPage)
    }

}