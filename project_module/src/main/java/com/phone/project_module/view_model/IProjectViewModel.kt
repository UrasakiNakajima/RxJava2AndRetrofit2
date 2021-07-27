package com.phone.project_module.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.phone.project_module.bean.DataX

interface IProjectViewModel {

    fun projectData(fragment: Fragment, currentPage: String)

    fun getDataxSuccess(): MutableLiveData<List<DataX>>

    fun getDataxError(): MutableLiveData<String>
}