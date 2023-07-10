package com.phone.call_third_party_so.iprovider

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.call_third_party_so.bean.SubDataSquare

interface ISquareProvider : IProvider {

    var mSquareDataList: List<SubDataSquare>

}