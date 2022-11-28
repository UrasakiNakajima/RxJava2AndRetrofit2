package com.phone.first_page_module

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.bean.FirstPageResponse.ResultData.JuheNewsBean
import com.phone.common_library.service.IFirstPageService

@Route(path = "/first_page_module/FirstPageServiceImpl")
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