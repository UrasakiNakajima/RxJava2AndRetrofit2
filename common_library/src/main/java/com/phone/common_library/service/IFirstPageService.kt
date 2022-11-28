package com.phone.common_library.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.common_library.bean.FirstPageResponse.ResultData.JuheNewsBean

interface IFirstPageService : IProvider {

    var mFirstPageDataList: MutableList<JuheNewsBean?>
        get() = mutableListOf()
        set(value) = TODO()

    fun setFirstPageDataList(firstPageDataList: MutableList<JuheNewsBean?>)

    fun getFirstPageDataList(): MutableList<JuheNewsBean?>
}