package com.phone.module_home

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.common.ConstantData
import com.phone.library_common.service.IWhichPage


@Route(path = ConstantData.Route.ROUTE_TO_WHICH_PAGE)
class WhichPageImpl : IWhichPage {

    companion object {
        private val TAG = WhichPageImpl::class.java.simpleName
    }

    override var mWhichPage: String = "Home"
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }

}