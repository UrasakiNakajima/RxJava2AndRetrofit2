package com.phone.project_module.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IProjectModel {

    fun projectData(currentPage: String): Observable<ResponseBody>

}