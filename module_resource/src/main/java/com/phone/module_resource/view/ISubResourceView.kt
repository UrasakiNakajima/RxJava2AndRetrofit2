package com.phone.module_resource.view

import com.phone.library_base.base.IBaseView
import com.phone.library_custom_view.bean.ArticleListBean

interface ISubResourceView : IBaseView {

    fun subResourceDataSuccess(success: MutableList<ArticleListBean>)

    fun subResourceDataError(error: String)

}