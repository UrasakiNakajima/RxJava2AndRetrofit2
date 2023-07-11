package com.phone.library_common.iprovider

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.ResultData

interface IHomeProvider : IProvider {

    var mHomeDataList: MutableList<ResultData.JuheNewsBean>

}