package com.phone.library_common.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.HomePageResponse.ResultData.JuheNewsBean

interface IHomeService : IProvider {

    var mHomeDataList: MutableList<JuheNewsBean>

}