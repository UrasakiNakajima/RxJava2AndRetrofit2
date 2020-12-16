package com.mobile.rxjava2andretrofit2.kotlin.mine.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans

interface IResourcesView : IBaseView {

    fun resourcesDataSuccess(success: List<Ans>)

    fun resourcesDataError(error: String?)

}