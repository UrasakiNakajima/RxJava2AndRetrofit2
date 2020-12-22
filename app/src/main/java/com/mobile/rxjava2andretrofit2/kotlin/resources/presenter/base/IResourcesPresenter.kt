package com.mobile.rxjava2andretrofit2.kotlin.resources.presenter.base

interface IResourcesPresenter {

    fun resourcesData(type: String, pageSize: String, currentPage: String)

}