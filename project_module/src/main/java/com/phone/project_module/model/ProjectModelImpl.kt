package com.phone.project_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.project_module.request.ProjectRequest
import io.reactivex.Observable
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