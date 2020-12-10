package com.mobile.rxjava2andretrofit2.kotlin.mine.view

import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Data

interface IMineDetailsView : IBaseView {

    fun mineDetailsSuccess(success: List<Data>);

    fun mineDetailsError(error: String?);
}