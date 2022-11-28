package com.phone.first_page_module.presenter

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

interface IFirstPagePresenter {

    fun firstPage(rxFragment: RxFragment, bodyParams: Map<String, String>)

    fun firstPage2(rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>)

}