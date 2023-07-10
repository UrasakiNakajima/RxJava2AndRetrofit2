package com.phone.module_mine.view

import com.phone.library_base.base.IBaseView
import com.phone.call_third_party_so.bean.Data

interface IMineView : IBaseView {

    fun mineDataSuccess(success: MutableList<Data>)

    fun mineDataError(error: String)

}