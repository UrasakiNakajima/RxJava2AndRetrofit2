package com.phone.resource_module.ui

import androidx.core.content.ContextCompat
import com.phone.common_library.base.BaseRxAppActivity
import com.phone.resource_module.R
import kotlinx.android.synthetic.main.activity_resource_details.*

class ResourceDetailsActivity : BaseRxAppActivity() {

    override fun initLoadData() {

    }

    override fun initLayoutId(): Int {
        return R.layout.activity_resource_details
    }

    override fun initViews() {
        setToolbar(false, R.color.color_FF198CFF)
        imv_back.setColorFilter(ContextCompat.getColor(rxAppCompatActivity!!,R.color.color_FFFFFFFF))
        layout_back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {

    }


}