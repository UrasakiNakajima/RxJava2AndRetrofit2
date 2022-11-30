package com.phone.module_project.model

import okhttp3.ResponseBody
import retrofit2.Call

interface IProjectModel {

    fun projectTabData(): Call<ResponseBody>

}