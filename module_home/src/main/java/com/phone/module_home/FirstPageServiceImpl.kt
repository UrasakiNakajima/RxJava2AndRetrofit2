package com.phone.module_home

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.bean.FirstPageResponse.ResultData.JuheNewsBean
import com.phone.library_common.service.IFirstPageService

@Route(path = "/module_home/FirstPageServiceImpl")
class FirstPageServiceImpl : IFirstPageService {

    private val TAG = FirstPageServiceImpl::class.java.simpleName

    override var mFirstPageDataList: MutableList<JuheNewsBean> = mutableListOf()
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }

}