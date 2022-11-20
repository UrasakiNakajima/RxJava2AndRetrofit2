package com.phone.project_module.view_model

import androidx.lifecycle.MutableLiveData
import com.phone.project_module.bean.DataX
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface IProjectViewModel {

    fun projectDataRxFragment(rxFragment: RxFragment, currentPage: String)

    fun getDataxRxFragmentSuccess(): MutableLiveData<List<DataX>>

    fun getDataxRxFragmentError(): MutableLiveData<String>

    fun projectDataRxAppCompatActivity(rxAppCompatActivity: RxAppCompatActivity, currentPage: String)

    fun getDataxRxAppCompatActivitySuccess(): MutableLiveData<List<DataX>>

    fun getDataxRxAppCompatActivityError(): MutableLiveData<String>

}