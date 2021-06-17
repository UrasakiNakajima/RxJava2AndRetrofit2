package com.mobile.mine_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.mine_module.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: List<Data>)

    fun mineDataError(error: String)

}