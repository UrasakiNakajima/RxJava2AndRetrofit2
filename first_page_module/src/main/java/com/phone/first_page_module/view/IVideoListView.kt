package com.phone.first_page_module.view

import com.phone.common_library.base.IBaseView

interface IVideoListView : IBaseView {

    fun videoListSuccess(success: String)

    fun videoListError(error: String)

}