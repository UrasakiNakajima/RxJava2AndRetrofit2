package com.phone.module_project.model

import com.phone.library_common.manager.RetrofitManager
import com.phone.module_project.request.ProjectRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ProjectModelImpl : IProjectModel {

    private val TAG = ProjectModelImpl::class.java.simpleName

    override fun projectTabData(): Call<ResponseBody> {
        return RetrofitManager.get().mRetrofit
            .create(ProjectRequest::class.java)
            .getProjectTabData()
    }

}