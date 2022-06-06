package com.phone.square_module.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.phone.square_module.bean.DataX
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface ISquareViewModel {

    fun squareDataRxFragment(rxFragment: RxFragment, currentPage: String)

    fun getDataxRxFragmentSuccess(): MutableLiveData<List<DataX>>

    fun getDataxRxFragmentError(): MutableLiveData<String>

    fun squareDataRxAppCompatActivity(rxAppCompatActivity: RxAppCompatActivity, currentPage: String)

    fun getDataxRxAppCompatActivitySuccess(): MutableLiveData<List<DataX>>

    fun getDataxRxAppCompatActivityError(): MutableLiveData<String>

}