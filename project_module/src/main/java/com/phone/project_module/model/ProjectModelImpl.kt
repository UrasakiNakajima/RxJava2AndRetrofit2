package com.phone.project_module.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.project_module.request.ProjectRequest
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