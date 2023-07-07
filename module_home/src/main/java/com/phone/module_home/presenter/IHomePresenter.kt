package com.phone.module_home.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

interface IHomePresenter {

    fun homePage(rxFragment: RxFragment, bodyParams: Map<String, String>)

}