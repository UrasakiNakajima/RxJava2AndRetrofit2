package com.phone.project_module.view

import com.phone.common_library.base.IBaseView
import com.phone.project_module.bean.DataX

interface IProjectChildView : IBaseView {

    fun projectDataSuccess(success: List<DataX>)

    fun projectDataError(error: String)

}