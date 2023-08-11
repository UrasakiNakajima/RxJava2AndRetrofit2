package com.phone.module_project.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_custom_view.bean.ArticleBean
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_project.request.ProjectRequest
import okhttp3.ResponseBody
import retrofit2.Call

class SubProjectModelImpl : ISubProjectModel {

    companion object {
        private val TAG: String = SubProjectModelImpl::class.java.simpleName
    }

    override suspend fun subProjectData(
        pageNum: Int,
        tabId: Int
    ): ApiResponse<ArticleBean> {
        return RetrofitManager.instance.mRetrofit
            .create(ProjectRequest::class.java)
            .getSubProjectData(pageNum, tabId)
    }

    override suspend fun subProjectData2(
        pageNum: Int,
        tabId: Int
    ): Call<ResponseBody> {
        return RetrofitManager.instance.mRetrofit
            .create(ProjectRequest::class.java)
            .getSubProjectData2(pageNum, tabId)
    }

}