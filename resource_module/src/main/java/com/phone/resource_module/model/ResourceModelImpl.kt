package com.phone.resource_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.resource_module.model.base.IResourceModel
import com.phone.resource_module.request.ResourceRequest
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