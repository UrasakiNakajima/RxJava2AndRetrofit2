package com.phone.module_home.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_mvvm.BaseBindingRxAppActivity
import com.phone.library_common.common.ConstantData
import com.phone.module_home.R
import com.phone.module_home.databinding.HomeActivityHomeBinding
import com.phone.module_home.fragment.HomeFragment

@Route(path = ConstantData.Route.ROUTE_HOME)
class HomeActivity : BaseBindingRxAppActivity<HomeActivityHomeBinding>() {

    companion object{
        private val TAG = HomeActivity::class.java.simpleName
    }

    override fun initLayoutId(): Int {
        return R.layout.home_activity_home
    }

    override fun initData() {

    }

    override fun initViews() {
        setToolbar(false, R.color.library_color_FF198CFF)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, HomeFragment()) // 此处的R.id.fragment_container是要盛放fragment的父容器
            .commit()
    }

    override fun initLoadData() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

}