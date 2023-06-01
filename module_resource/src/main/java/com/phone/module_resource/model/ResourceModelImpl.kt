package com.phone.module_resource.model

import com.phone.library_common.manager.RetrofitManager
import com.phone.module_resource.request.ResourceRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ResourceModelImpl : IResourceModel {

    private val TAG = ResourceModelImpl::class.java.simpleName

    override fun resourceTabData(): Call<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(ResourceRequest::class.java)
            .getResourceTabData()
    }

}