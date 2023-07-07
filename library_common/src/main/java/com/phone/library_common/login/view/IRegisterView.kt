package com.phone.library_common.login.view

import com.phone.library_common.base.IBaseView

interface IRegisterView:IBaseView {

    fun registerSuccess(success: String)

    fun registerError(error: String)

}