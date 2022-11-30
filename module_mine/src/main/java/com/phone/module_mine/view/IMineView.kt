package com.phone.module_mine.view

import com.phone.library_common.base.IBaseView
import com.phone.module_mine.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: MutableList<Data>)

    fun mineDataError(error: String)

}