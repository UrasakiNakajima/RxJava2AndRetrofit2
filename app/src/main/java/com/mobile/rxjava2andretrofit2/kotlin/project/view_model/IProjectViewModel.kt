package com.mobile.rxjava2andretrofit2.kotlin.project.view_model

import androidx.lifecycle.MutableLiveData
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX

interface IProjectViewModel {

    fun projectData(currentPage: String)

    fun getDataxSuccess(): MutableLiveData<List<DataX>>

    fun getDataxError(): MutableLiveData<String>
}