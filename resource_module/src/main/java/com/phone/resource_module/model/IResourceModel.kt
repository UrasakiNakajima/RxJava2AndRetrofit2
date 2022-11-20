package com.phone.resource_module.model

import okhttp3.ResponseBody
import retrofit2.Call

interface IResourceModel {

    fun resourceTabData(): Call<ResponseBody>

}