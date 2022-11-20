package com.phone.resource_module.view

import com.phone.common_library.base.IBaseView
import com.phone.resource_module.bean.ArticleListBean

interface IResourceChildView : IBaseView {

    fun resourceDataSuccess(success: MutableList<ArticleListBean>)

    fun resourceDataError(error: String)

}