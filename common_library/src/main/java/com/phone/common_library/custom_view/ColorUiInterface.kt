package com.phone.common_library.custom_view

import android.content.res.Resources.Theme
import android.view.View

/**
 * 换肤接口
 * Created by chengli on 15/6/8.
 */
interface ColorUiInterface {

    fun getView(): View?

    fun setTheme(themeId: Theme?)
}