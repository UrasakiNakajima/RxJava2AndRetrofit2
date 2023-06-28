package com.phone.module_resource.model

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.ArticleBean
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_resource.request.ResourceRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class SubResourceModelImpl : ISubResourceModel {

    private val TAG = SubResourceModelImpl::class.java.simpleName

    override suspend fun subResourceData(
        tabId: Int,
        pageNum: Int
    ): ApiResponse<ArticleBean> {
        return RetrofitManager.instance().mRetrofit
            .create(ResourceRequest::class.java)
            .getSubResourceData(tabId, pageNum)
    }

    override suspend fun subResourceData2(
        tabId: Int,
        pageNum: Int
    ): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(ResourceRequest::class.java)
            .getSubResourceData2(tabId, pageNum)
    }

}