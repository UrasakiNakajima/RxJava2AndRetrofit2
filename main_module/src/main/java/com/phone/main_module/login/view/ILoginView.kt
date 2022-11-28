package com.phone.main_module.login.view

import com.phone.common_library.base.IBaseView
import com.phone.main_module.login.bean.GetVerificationCode
import com.phone.main_module.login.bean.LoginResponse

interface ILoginView:IBaseView {

    fun getAuthCodeSuccess(success: GetVerificationCode.DataDTO?)

    fun getAuthCodeError(error: String?)

    fun loginWithAuthCodeSuccess(success: LoginResponse.DataDTO?)

    fun loginWithAuthCodeError(error: String?)

    fun loginSuccess(success: String?)

    fun loginError(error: String?)

}