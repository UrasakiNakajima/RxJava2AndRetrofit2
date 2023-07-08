package com.phone.module_resource.view

import com.phone.library_base.base.IBaseView
import com.phone.library_common.bean.TabBean

interface IResourceView : IBaseView {

    fun resourceTabDataSuccess(success: MutableList<TabBean>)

    fun resourceTabDataError(error: String)

}