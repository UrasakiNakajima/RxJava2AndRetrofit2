package com.mobile.rxjava2andretrofit2.kotlin.resources_show.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.resources_show.bean.Result

interface IResourcesView : IBaseView {

    fun resourcesDataSuccess(success: List<Result>)

    fun resourcesDataError(error: String)

}