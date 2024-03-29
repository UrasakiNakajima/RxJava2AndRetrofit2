package com.phone.module_mine.view

import com.phone.library_base.base.IBaseView

/**
 *    author : Urasaki
 *    e-mail : 1164688204@qq.com
 *    date   : 2021/5/1916:41
 *    desc   :
 *    version: 1.0
 */
interface IUserDataView : IBaseView {

    fun userDataSuccess(success: String)

    fun userDataError(error: String)

}