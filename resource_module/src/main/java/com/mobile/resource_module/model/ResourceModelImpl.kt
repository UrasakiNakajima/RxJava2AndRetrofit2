package com.mobile.resource_module.model

import com.mobile.common_library.manager.RetrofitManager
import com.mobile.resource_module.model.base.IResourceModel
import com.mobile.resource_module.request.ResourceRequest
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