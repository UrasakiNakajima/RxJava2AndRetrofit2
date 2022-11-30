package com.phone.module_resource.view

import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.ArticleListBean

interface ISubResourceView : IBaseView {

    fun subResourceDataSuccess(success: MutableList<ArticleListBean>)

    fun subResourceDataError(error: String)

}