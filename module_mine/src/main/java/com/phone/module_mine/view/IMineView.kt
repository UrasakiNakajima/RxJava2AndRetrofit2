package com.phone.module_mine.view

import com.phone.common_library.base.IBaseView
import com.phone.module_mine.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: MutableList<Data>)

    fun mineDataError(error: String)

}