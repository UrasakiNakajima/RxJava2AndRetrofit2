package com.phone.resource_module.view

import com.phone.common_library.base.IBaseView
import com.phone.common_library.bean.ArticleListBean

interface ISubResourceView : IBaseView {

    fun subResourceDataSuccess(success: MutableList<ArticleListBean>)

    fun subResourceDataError(error: String)

}