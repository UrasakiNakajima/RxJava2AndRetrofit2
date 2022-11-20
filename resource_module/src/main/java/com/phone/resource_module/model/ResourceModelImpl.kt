package com.phone.resource_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.resource_module.request.ResourceRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ResourceModelImpl : IResourceModel {

    private val TAG = ResourceModelImpl::class.java.simpleName

    override fun resourceTabData(): Call<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(ResourceRequest::class.java)
            .getResourceTabData()
    }

}