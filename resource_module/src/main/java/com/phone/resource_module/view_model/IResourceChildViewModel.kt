package com.phone.square_module.view_model

import com.trello.rxlifecycle3.components.support.RxFragment

interface IResourceChildViewModel {

    fun resourceData(rxFragment: RxFragment, tabId: Int, pageNum: Int)

}