package com.phone.library_common.iprovider

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.SubDataSquare

interface ISquareProvider : IProvider {

    var mSquareDataList: List<SubDataSquare>

}