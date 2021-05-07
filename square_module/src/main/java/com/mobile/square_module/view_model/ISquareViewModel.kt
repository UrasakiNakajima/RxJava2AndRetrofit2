package com.mobile.square_module.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.mobile.square_module.bean.DataX

interface ISquareViewModel {

    fun squareData(fragment: Fragment, currentPage: String)

    fun getDataxSuccess(): MutableLiveData<List<DataX>>

    fun getDataxError(): MutableLiveData<String>

    fun squareDetails(currentPage: String)

    fun getDataxDetailsSuccess(): MutableLiveData<List<DataX>>

    fun getDataxDetailsError(): MutableLiveData<String>

}