package com.phone.resource_module.ui

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.resource_module.R

class WebViewActivity : BaseRxAppActivity() {

    private val TAG = WebViewActivity::class.java.simpleName

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: ViewDataBinding

    override fun initLayoutId() = R.layout.activity_web_view

    override fun initData() {
        mDatabind = DataBindingUtil.setContentView(this, initLayoutId())
        mDatabind.lifecycleOwner = this
    }

    override fun initViews() {
//        mDatabind
    }

    override fun initLoadData() {

    }
}