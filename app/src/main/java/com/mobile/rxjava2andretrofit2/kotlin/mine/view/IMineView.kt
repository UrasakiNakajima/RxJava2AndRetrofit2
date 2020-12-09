package com.mobile.rxjava2andretrofit2.kotlin.mine.view

import com.mobile.rxjava2andretrofit2.java.base.IBaseView
import com.mobile.rxjava2andretrofit2.java.mine.bean.MineResponse

interface IMineView : IBaseView {

    fun mineDataSuccess(success: List<MineResponse.AnsListBean>)

    fun mineDataError(error: String?)

}