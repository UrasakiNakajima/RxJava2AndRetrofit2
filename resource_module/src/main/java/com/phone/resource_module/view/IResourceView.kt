package com.phone.resource_module.view

import com.phone.common_library.base.IBaseView
import com.phone.resource_module.bean.TabBean

interface IResourceView : IBaseView {

    fun resourceTabDataSuccess(success: MutableList<TabBean>)

    fun resourceTabDataError(error: String)

}