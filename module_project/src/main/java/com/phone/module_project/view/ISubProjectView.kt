package com.phone.module_project.view

import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.ArticleListBean

interface ISubProjectView : IBaseView {

    fun subProjectDataSuccess(success: MutableList<ArticleListBean>)

    fun subProjectDataError(error: String)

}