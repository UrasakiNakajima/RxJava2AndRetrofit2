package com.phone.module_mine.view

import com.phone.library_base.base.IBaseView
import com.phone.library_common.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: MutableList<Data>)

    fun mineDataError(error: String)

}