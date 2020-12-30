package com.mobile.rxjava2andretrofit2.kotlin.project.model

import io.reactivex.Observable
import okhttp3.ResponseBody

interface IProjectModel {

    fun projectData(currentPage: String): Observable<ResponseBody>

}