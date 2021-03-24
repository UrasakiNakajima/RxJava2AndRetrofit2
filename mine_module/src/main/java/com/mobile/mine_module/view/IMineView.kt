package com.mobile.mine_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.mine_module.bean.Ans

interface IMineView : IBaseView {

    fun mineDataSuccess(success: List<Ans>)

    fun mineDataError(error: String)

}