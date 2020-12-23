package com.mobile.rxjava2andretrofit2.kotlin.resources_show.presenter.base

interface IResourcesPresenter {

    fun resourcesData(type: String, pageSize: String, currentPage: String)

}