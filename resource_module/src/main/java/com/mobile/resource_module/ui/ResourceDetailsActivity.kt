package com.mobile.resource_module.ui

import com.mobile.common_library.base.BaseAppActivity
import com.mobile.resource_module.R
import kotlinx.android.synthetic.main.activity_resource_details.*

class ResourceDetailsActivity : BaseAppActivity() {

    override fun initLoadData() {

    }

    override fun initLayoutId(): Int {
        return R.layout.activity_resource_details
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
        imv_back.setColorFilter(resources.getColor(R.color.color_FFFFFFFF))
        layout_back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {

    }


}