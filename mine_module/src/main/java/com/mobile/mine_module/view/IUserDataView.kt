package com.mobile.mine_module.view

import com.mobile.common_library.base.IBaseView
import com.mobile.mine_module.bean.Ans

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/1916:41
 *    desc   :
 *    version: 1.0
 */
interface IUserDataView : IBaseView {

    fun userDataSuccess(success: List<Ans>)

    fun userDataError(error: String)

}