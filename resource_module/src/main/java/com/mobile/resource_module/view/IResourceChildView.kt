package com.mobile.resource_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.resource_module.bean.Result2

interface IResourceChildView : IBaseView {

    fun resourceDataSuccess(success: List<Result2>)

    fun resourceDataError(error: String)

}