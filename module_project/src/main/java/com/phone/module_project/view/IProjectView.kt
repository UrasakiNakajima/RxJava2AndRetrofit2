package com.phone.module_project.view

import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.TabBean

interface IProjectView : IBaseView {

    fun projectTabDataSuccess(success: MutableList<TabBean>)

    fun projectTabDataError(error: String)

}