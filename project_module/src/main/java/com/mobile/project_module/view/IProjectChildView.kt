package com.mobile.project_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.project_module.bean.DataX

interface IProjectChildView : IBaseView {

    fun projectDataSuccess(success: List<DataX>)

    fun projectDataError(error: String)

}