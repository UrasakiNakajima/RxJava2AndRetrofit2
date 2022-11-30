package com.phone.module_home.view

import com.phone.library_common.base.IBaseView

interface IVideoListView : IBaseView {

    fun videoListSuccess(success: String)

    fun videoListError(error: String)

}