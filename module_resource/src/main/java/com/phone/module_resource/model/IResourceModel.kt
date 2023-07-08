package com.phone.module_resource.model

import com.phone.library_network.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import okhttp3.ResponseBody
import retrofit2.Call

interface IResourceModel {

    suspend fun resourceTabData(): ApiResponse<MutableList<TabBean>>

    suspend fun resourceTabData2(): Call<ResponseBody>

}