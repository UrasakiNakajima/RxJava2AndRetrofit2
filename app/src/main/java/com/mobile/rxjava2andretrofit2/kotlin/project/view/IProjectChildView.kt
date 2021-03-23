package com.mobile.rxjava2andretrofit2.kotlin.project.view

import com.mobile.common_library.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX

interface IProjectChildView : IBaseView {

    fun projectDataSuccess(success: List<DataX>)

    fun projectDataError(error: String)

}