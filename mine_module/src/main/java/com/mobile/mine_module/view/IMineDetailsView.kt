package com.mobile.mine_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.mine_module.bean.Data

interface IMineDetailsView : IBaseView {

    fun mineDetailsSuccess(success: List<Data>);

    fun mineDetailsError(error: String);
}