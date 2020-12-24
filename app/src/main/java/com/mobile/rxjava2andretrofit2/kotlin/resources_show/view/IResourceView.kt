package com.mobile.rxjava2andretrofit2.kotlin.resources_show.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.resources_show.bean.Result

interface IResourceView : IBaseView {

    fun resourceDataSuccess(success: List<Result>)

    fun resourceDataError(error: String)

}