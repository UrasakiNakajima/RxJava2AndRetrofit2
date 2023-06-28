package com.phone.module_home

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.bean.ResultData
import com.phone.library_common.common.ConstantData
import com.phone.library_common.service.IHomeService

@Route(path = ConstantData.Route.ROUTE_HOME_SERVICE)
class HomeServiceImpl : IHomeService {

    private val TAG = HomeServiceImpl::class.java.simpleName

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