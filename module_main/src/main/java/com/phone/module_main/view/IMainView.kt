package com.phone.module_main.view

import com.phone.library_common.base.IBaseView

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:20
 * introduce :
 */

interface IMainView: IBaseView {

    fun mainDataSuccess(success: String)

    fun mainDataError(error: String)
}