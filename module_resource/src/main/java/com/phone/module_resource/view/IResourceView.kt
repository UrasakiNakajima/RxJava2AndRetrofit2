package com.phone.module_resource.view

import com.phone.library_base.base.IBaseView
import com.phone.call_third_party_so.bean.TabBean

interface IResourceView : IBaseView {

    fun resourceTabDataSuccess(success: MutableList<TabBean>)

    fun resourceTabDataError(error: String)

}