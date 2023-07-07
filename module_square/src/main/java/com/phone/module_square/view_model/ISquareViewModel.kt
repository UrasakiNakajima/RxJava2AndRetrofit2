package com.phone.module_square.view_model

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

interface ISquareViewModel {

    fun squareData(rxFragment: RxFragment, currentPage: String)

}