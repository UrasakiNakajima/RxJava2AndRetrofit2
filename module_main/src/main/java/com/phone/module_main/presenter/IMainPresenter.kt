package com.phone.module_main.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/10 17:15
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

interface IMainPresenter {

    fun mainData(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)
}