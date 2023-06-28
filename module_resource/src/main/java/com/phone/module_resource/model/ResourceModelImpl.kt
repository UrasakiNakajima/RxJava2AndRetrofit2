package com.phone.module_resource.model

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_resource.request.ResourceRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ResourceModelImpl : IResourceModel {

    private val TAG = ResourceModelImpl::class.java.simpleName

    override suspend fun resourceTabData(): ApiResponse<MutableList<TabBean>> {
        return RetrofitManager.instance().mRetrofit
            .create(ResourceRequest::class.java)
            .getResourceTabData()
    }

    override suspend fun resourceTabData2(): Call<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(ResourceRequest::class.java)
            .getResourceTabData2()
    }

}