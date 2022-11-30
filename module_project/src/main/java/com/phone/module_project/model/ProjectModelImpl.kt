package com.phone.module_project.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.module_project.request.ProjectRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ProjectModelImpl : IProjectModel {

    private val TAG = ProjectModelImpl::class.java.simpleName

    override fun projectTabData(): Call<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(ProjectRequest::class.java)
            .getProjectTabData()
    }

}