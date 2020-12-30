package com.mobile.rxjava2andretrofit2.kotlin.resource.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.resource.bean.Result

interface IResourceChildView : IBaseView {

    fun resourceDataSuccess(success: List<Result>)

    fun resourceDataError(error: String)

}