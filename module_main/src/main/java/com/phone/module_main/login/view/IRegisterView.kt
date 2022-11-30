package com.phone.module_main.login.view

import com.phone.library_common.base.IBaseView

interface IRegisterView:IBaseView {

    fun registerSuccess(success: String)

    fun registerError(error: String)

}