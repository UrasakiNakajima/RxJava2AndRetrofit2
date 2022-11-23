package com.phone.project_module.view

import com.phone.common_library.base.IBaseView
import com.phone.common_library.bean.ArticleListBean

interface ISubProjectView : IBaseView {

    fun subProjectDataSuccess(success: MutableList<ArticleListBean>)

    fun subProjectDataError(error: String)

}