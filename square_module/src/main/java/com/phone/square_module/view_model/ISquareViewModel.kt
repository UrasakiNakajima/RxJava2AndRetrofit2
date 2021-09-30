package com.phone.square_module.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.phone.square_module.bean.DataX
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface ISquareViewModel {

    fun squareData(rxFragment: RxFragment, currentPage: String)

    fun getDataxSuccess(): MutableLiveData<List<DataX>>

    fun getDataxError(): MutableLiveData<String>

    fun squareDetails(rxAppCompatActivity: RxAppCompatActivity, currentPage: String)

    fun getDataxDetailsSuccess(): MutableLiveData<List<DataX>>

    fun getDataxDetailsError(): MutableLiveData<String>

}