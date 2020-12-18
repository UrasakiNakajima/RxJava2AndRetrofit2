package com.mobile.rxjava2andretrofit2.kotlin.resources.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans
import com.mobile.rxjava2andretrofit2.kotlin.resources.bean.Result

interface IResourcesView : IBaseView {

    fun resourcesDataSuccess(success: List<Result>)

    fun resourcesDataError(error: String?)

}