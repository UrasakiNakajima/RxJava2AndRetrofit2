package com.phone.project_module.view

import com.phone.common_library.base.IBaseView
import com.phone.common_library.bean.TabBean

interface IProjectView : IBaseView {

    fun projectTabDataSuccess(success: MutableList<TabBean>)

    fun projectTabDataError(error: String)

}