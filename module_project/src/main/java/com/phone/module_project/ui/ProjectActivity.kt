package com.phone.module_project.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.module_project.R
import com.phone.module_project.databinding.ProjectActivityProjectBinding

@Route(path = ConstantData.Route.ROUTE_PROJECT)
class ProjectActivity :
    BaseBindingRxAppActivity<ProjectActivityProjectBinding>() {
    companion object {
        private val TAG: String = ProjectActivity::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.project_activity_project

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)

    }

    override fun initLoadData() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

}