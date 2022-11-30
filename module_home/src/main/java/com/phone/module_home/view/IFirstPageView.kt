package com.phone.module_home.view

import com.phone.common_library.base.IBaseView
import com.phone.common_library.bean.FirstPageResponse.ResultData.JuheNewsBean

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */
interface IFirstPageView:IBaseView {

    fun firstPageDataSuccess(success: List<JuheNewsBean>)

    fun firstPageDataError(error: String)
}