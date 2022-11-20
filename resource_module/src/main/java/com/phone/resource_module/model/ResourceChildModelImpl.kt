package com.phone.resource_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.resource_module.request.ResourceRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class ResourceChildModelImpl : IResourceChildModel {

    private val TAG = ResourceChildModelImpl::class.java.simpleName

    override fun resourceData(
        type: String,
        pageSize: String,
        currentPage: String
    ): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(ResourceRequest::class.java)
            .getResourceData(type, pageSize, currentPage)
    }

    override fun resourceData2(
        tabId: Int,
        pageNum: Int
    ): Call<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(ResourceRequest::class.java)
            .getResourceData2(tabId, pageNum)
    }

}