package com.mobile.rxjava2andretrofit2.kotlin.resource.presenter.base

interface IResourcePresenter {

    fun resourceData(type: String, pageSize: String, currentPage: String)

}