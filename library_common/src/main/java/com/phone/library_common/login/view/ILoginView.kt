package com.phone.library_common.login.view

import com.phone.library_common.base.IBaseView
import com.phone.library_common.login.bean.DataGetVerification
import com.phone.library_common.login.bean.DataLogin

interface ILoginView : IBaseView {

    fun getAuthCodeSuccess(success: DataGetVerification)

    fun getAuthCodeError(error: String)

    fun loginWithAuthCodeSuccess(success: DataLogin)

    fun loginWithAuthCodeError(error: String)

    fun loginSuccess(success: String)

    fun loginError(error: String)

}