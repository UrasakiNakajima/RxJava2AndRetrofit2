package com.phone.module_project.model

import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_project.request.ProjectRequest
import okhttp3.ResponseBody
import retrofit2.Call

class ProjectModelImpl : IProjectModel {

    private val TAG = ProjectModelImpl::class.java.simpleName

    override suspend fun projectTabData(): ApiResponse<MutableList<TabBean>> {
        return RetrofitManager.instance().mRetrofit.create(ProjectRequest::class.java)
            .getProjectTabData()
    }

    override fun projectTabData2(): Call<ResponseBody> {
        return RetrofitManager.instance().mRetrofit.create(ProjectRequest::class.java)
            .getProjectTabData2()
    }

}