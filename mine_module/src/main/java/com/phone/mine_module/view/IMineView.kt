package com.phone.mine_module.view

import com.phone.common_library.base.IBaseView
import com.phone.mine_module.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: MutableList<Data>)

    fun mineDataError(error: String)

}