package com.phone.library_login.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

interface ILoginPresenter {

    fun getAuthCode(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

    fun loginWithAuthCode(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    )

    fun login(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

    fun register(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

}