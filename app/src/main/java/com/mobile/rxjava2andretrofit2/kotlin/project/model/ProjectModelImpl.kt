package com.mobile.rxjava2andretrofit2.kotlin.project.model

import com.mobile.common_library.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.project.request.ProjectRequest
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