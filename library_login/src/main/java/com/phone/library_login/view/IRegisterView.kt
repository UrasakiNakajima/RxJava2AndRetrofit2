package com.phone.library_login.view

import com.phone.library_base.base.IBaseView

interface IRegisterView: IBaseView {

    fun registerSuccess(success: String)

    fun registerError(error: String)

}