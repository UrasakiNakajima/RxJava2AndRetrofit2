package com.phone.main_module.login.view

import com.phone.common_library.base.IBaseView

interface IRegisterView:IBaseView {

    fun registerSuccess(success: String?)

    fun registerError(error: String?)

}