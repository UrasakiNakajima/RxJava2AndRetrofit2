package com.phone.module_project.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import okhttp3.ResponseBody
import retrofit2.Call

interface IProjectModel {

    suspend fun projectTabData(): ApiResponse<MutableList<TabBean>>

    fun projectTabData2(): Call<ResponseBody>

}