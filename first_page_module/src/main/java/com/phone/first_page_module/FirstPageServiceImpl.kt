package com.phone.first_page_module

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.bean.FirstPageResponse.ResultData.JuheNewsBean
import com.phone.common_library.service.IFirstPageService

@Route(path = "/first_page_module/FirstPageServiceImpl")
class FirstPageServiceImpl : IFirstPageService {

    private val TAG = FirstPageServiceImpl::class.java.simpleName

    override fun setFirstPageDataList(firstPageDataList: MutableList<JuheNewsBean?>) {
        mFirstPageDataList.clear()
        mFirstPageDataList.addAll(firstPageDataList)
    }

    override fun getFirstPageDataList(): MutableList<JuheNewsBean?> {
        return mFirstPageDataList
    }

    override fun init(context: Context?) {
    }

}