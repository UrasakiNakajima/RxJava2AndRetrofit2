package com.phone.module_home.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.bean.ResultData
import com.phone.library_common.common.ConstantData
import com.phone.library_common.iprovider.IHomeProvider

@Route(path = ConstantData.Route.ROUTE_HOME_SERVICE)
class HomeProviderImpl : IHomeProvider {

    private val TAG = HomeProviderImpl::class.java.simpleName

    override var mHomeDataList: MutableList<ResultData.JuheNewsBean> = mutableListOf()
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }

}