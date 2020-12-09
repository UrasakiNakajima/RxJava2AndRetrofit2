package com.mobile.rxjava2andretrofit2.kotlin.mine.view

import com.mobile.rxjava2andretrofit2.java.base.IBaseView
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.Data
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineDetailsResponse

interface IMineDetailsView : IBaseView {

    fun mineDetailsSuccess(success: List<Data>);

    fun mineDetailsError(error: String?);
}