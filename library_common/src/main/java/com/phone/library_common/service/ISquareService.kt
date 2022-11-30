package com.phone.library_common.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.DataX

interface ISquareService : IProvider {

    var mSquareDataList: List<DataX>

}