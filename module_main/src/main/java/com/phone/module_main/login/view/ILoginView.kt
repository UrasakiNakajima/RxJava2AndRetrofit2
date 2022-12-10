package com.phone.module_main.login.view

import com.phone.library_common.base.IBaseView
import com.phone.module_main.login.DataGetVerification
import com.phone.module_main.login.DataLogin

interface ILoginView : IBaseView {

    fun getAuthCodeSuccess(success: DataGetVerification)

    fun getAuthCodeError(error: String)

    fun loginWithAuthCodeSuccess(success: DataLogin)

    fun loginWithAuthCodeError(error: String)

    fun loginSuccess(success: String)

    fun loginError(error: String)

}