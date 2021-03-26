package com.mobile.project_module.view_model

import androidx.lifecycle.MutableLiveData
import com.mobile.project_module.bean.DataX

interface IProjectViewModel {

    fun projectData(currentPage: String)

    fun getDataxSuccess(): MutableLiveData<List<DataX>>

    fun getDataxError(): MutableLiveData<String>
}