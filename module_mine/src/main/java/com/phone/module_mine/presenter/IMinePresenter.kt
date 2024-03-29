package com.phone.module_mine.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface IMinePresenter {

    fun mineData(rxFragment: RxFragment, bodyParams: Map<String, String>)

    fun userData(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

    fun userData(rxAppCompatActivity: RxAppCompatActivity, accessToken: String, bodyParams: Map<String, String>)
}