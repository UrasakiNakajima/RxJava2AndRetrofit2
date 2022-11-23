package com.phone.resource_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.resource_module.request.ResourceRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class SubResourceModelImpl : ISubResourceModel {

    private val TAG = SubResourceModelImpl::class.java.simpleName

    override fun subResourceData(
        tabId: Int,
        pageNum: Int
    ): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(ResourceRequest::class.java)
            .getSubResourceData(tabId, pageNum)
    }

    override fun subResourceData2(
        tabId: Int,
        pageNum: Int
    ): Call<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
            .create(ResourceRequest::class.java)
            .getSubResourceData2(tabId, pageNum)
    }

}