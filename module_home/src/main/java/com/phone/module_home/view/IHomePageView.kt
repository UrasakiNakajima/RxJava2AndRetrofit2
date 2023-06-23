package com.phone.module_home.view

import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.HomePageResponse.ResultData.JuheNewsBean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */
interface IHomePageView : IBaseView {

    fun homePageDataSuccess(success: List<JuheNewsBean>)

    fun homePageDataError(error: String)
}