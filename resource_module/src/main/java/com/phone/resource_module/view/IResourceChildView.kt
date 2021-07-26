package com.phone.resource_module.view

import com.phone.common_library.base.IBaseView
import com.phone.resource_module.bean.Result2

interface IResourceChildView : IBaseView {

    fun resourceDataSuccess(success: List<Result2>)

    fun resourceDataError(error: String)

}