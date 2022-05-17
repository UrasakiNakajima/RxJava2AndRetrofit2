package com.phone.resource_module.presenter.base

import com.trello.rxlifecycle3.components.support.RxFragment

interface IResourcePresenter {

    fun resourceData(rxFragment: RxFragment, type: String, pageSize: String, currentPage: String)

}