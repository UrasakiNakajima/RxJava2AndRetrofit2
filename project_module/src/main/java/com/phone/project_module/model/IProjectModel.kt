package com.phone.project_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

interface IProjectModel {

    fun projectTabData(): Call<ResponseBody>

}