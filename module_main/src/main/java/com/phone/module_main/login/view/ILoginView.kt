package com.phone.module_main.login.view

import com.phone.common_library.base.IBaseView
import com.phone.module_main.login.bean.GetVerificationCode
import com.phone.module_main.login.bean.LoginResponse

interface ILoginView:IBaseView {

    fun getAuthCodeSuccess(success: GetVerificationCode.DataDTO)

    fun getAuthCodeError(error: String)

    fun loginWithAuthCodeSuccess(success: LoginResponse.DataDTO)

    fun loginWithAuthCodeError(error: String)

    fun loginSuccess(success: String)

    fun loginError(error: String)

}