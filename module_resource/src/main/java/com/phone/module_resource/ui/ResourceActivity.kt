package com.phone.module_resource.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.common.ConstantData
import com.phone.module_resource.R
import com.phone.module_resource.fragment.ResourceFragment
import com.phone.module_resource.databinding.ResourceActivityResourceBinding

@Route(path = ConstantData.Route.ROUTE_RESOURCE)
class ResourceActivity :
    BaseBindingRxAppActivity<ResourceActivityResourceBinding>() {

    private val TAG = ResourceActivity::class.java.simpleName

    override fun initLayoutId() = R.layout.resource_activity_resource

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, ResourceFragment()) // 此处的R.id.fragment_container是要盛放fragment的父容器
            .commit()
    }

    override fun initLoadData() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

}