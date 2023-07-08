package com.phone.module_resource.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_custom_view.bean.ArticleBean
import io.reactivex.Observable
import okhttp3.ResponseBody

interface ISubResourceModel {

    suspend fun subResourceData(
        tabId: Int,
        pageNum: Int
    ): ApiResponse<ArticleBean>

    suspend fun subResourceData2(
        tabId: Int,
        pageNum: Int
    ): Observable<ResponseBody>

}