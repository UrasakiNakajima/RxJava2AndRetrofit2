package com.phone.module_square.view_model

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface IFunctionMenuViewMode {

    fun downloadFile(rxAppCompatActivity: RxAppCompatActivity)

    fun insertBook(rxAppCompatActivity: RxAppCompatActivity, success: String)

    fun queryBook()

}