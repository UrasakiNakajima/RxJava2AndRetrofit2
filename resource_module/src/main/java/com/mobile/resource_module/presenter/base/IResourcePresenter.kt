package com.mobile.resource_module.presenter.base

import androidx.fragment.app.Fragment

interface IResourcePresenter {

    fun resourceData(fragment: Fragment, type: String, pageSize: String, currentPage: String)

}