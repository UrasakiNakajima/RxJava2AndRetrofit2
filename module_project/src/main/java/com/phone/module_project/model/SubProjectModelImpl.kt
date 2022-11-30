package com.phone.module_project.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.module_project.request.ProjectRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

class SubProjectModelImpl : ISubProjectModel {

    companion object {
        private val TAG: String = SubProjectModelImpl::class.java.simpleName
    }

    override fun subProjectData(
        pageNum: Int,
        tabId: Int
    ): Observable<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(ProjectRequest::class.java)
            .getSubProjectData(pageNum, tabId)
    }

    override fun subProjectData2(
        pageNum: Int,
        tabId: Int
    ): Call<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(ProjectRequest::class.java)
            .getSubProjectData2(pageNum, tabId)
    }

}