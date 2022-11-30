package com.phone.library_common.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.phone.library_common.bean.FirstPageResponse.ResultData.JuheNewsBean

interface IFirstPageService : IProvider {

    var mFirstPageDataList: MutableList<JuheNewsBean>

}