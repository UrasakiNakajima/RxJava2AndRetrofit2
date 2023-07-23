package com.phone.module_square.view_model

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import io.reactivex.Observable
import okhttp3.ResponseBody

interface ISquareViewModel {

    fun squareData(rxFragment: RxFragment, currentPage: String)

    fun downloadFile(rxFragment: RxFragment)

    fun insertBook(rxFragment: RxFragment, success: String)

    fun queryBook()
}