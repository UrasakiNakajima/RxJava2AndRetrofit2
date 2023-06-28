package com.phone.library_common.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.ResultData

interface IHomeService : IProvider {

    var mHomeDataList: MutableList<ResultData.JuheNewsBean>

}