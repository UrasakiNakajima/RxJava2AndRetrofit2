package com.phone.module_project.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_custom_view.bean.ArticleBean
import okhttp3.ResponseBody
import retrofit2.Call

interface ISubProjectModel {

    suspend fun subProjectData(
        pageNum: Int,
        tabId: Int
    ): ApiResponse<ArticleBean>

    suspend fun subProjectData2(
        pageNum: Int,
        tabId: Int
    ): Call<ResponseBody>

}