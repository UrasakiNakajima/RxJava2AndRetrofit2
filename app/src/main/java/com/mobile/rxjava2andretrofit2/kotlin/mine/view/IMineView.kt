package com.mobile.rxjava2andretrofit2.kotlin.mine.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Ans

interface IMineView : IBaseView {

    fun mineDataSuccess(success: List<Ans>)

    fun mineDataError(error: String)

}