package com.phone.library_login.view

import com.phone.library_base.base.IBaseView
import com.phone.library_login.bean.DataGetVerification
import com.phone.library_login.bean.DataLogin

interface ILoginView : IBaseView {

    fun getAuthCodeSuccess(success: DataGetVerification)

    fun getAuthCodeError(error: String)

    fun loginWithAuthCodeSuccess(success: DataLogin)

    fun loginWithAuthCodeError(error: String)

    fun loginSuccess(success: String)

    fun loginError(error: String)

}