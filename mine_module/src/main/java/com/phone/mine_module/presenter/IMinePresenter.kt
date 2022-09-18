package com.phone.mine_module.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface IMinePresenter {

    fun mineDataRxFragment(rxFragment: RxFragment, bodyParams: Map<String, String>)

    fun mineDataRxAppCompatActivity(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

    fun userData(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

    fun userData(rxAppCompatActivity: RxAppCompatActivity, accessToken: String, bodyParams: Map<String, String>)
}