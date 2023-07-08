package com.phone.module_project.view

import com.phone.library_base.base.IBaseView
import com.phone.library_custom_view.bean.ArticleListBean

interface ISubProjectView : IBaseView {

    fun subProjectDataSuccess(success: MutableList<ArticleListBean>)

    fun subProjectDataError(error: String)

}