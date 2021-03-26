package com.mobile.project_module.model

import com.mobile.common_library.manager.RetrofitManager
import com.mobile.project_module.request.ProjectRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

class ProjectModelImpl() : IProjectModel {

    private val TAG: String = "ProjectModelImpl"

    override fun projectData(currentPage: String): Observable<ResponseBody> {
        return RetrofitManager.getInstance().retrofit
                .create(ProjectRequest::class.java)
                .getProjectData(currentPage)
    }

}