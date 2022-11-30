package com.phone.common_library.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.common_library.bean.DataX

interface ISquareService : IProvider {

    var mSquareDataList: List<DataX>

}