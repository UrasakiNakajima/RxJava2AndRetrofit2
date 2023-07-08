package com.phone.module_home.view

import com.phone.library_base.base.IBaseView
import com.phone.library_common.bean.ResultData

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */
interface IHomePageView : IBaseView {

    fun homePageDataSuccess(success: List<ResultData.JuheNewsBean>)

    fun homePageDataError(error: String)
}