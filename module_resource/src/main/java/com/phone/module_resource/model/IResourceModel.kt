package com.phone.module_resource.model

import okhttp3.ResponseBody
import retrofit2.Call

interface IResourceModel {

    fun resourceTabData(): Call<ResponseBody>

}