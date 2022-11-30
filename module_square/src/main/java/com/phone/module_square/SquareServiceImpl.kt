package com.phone.module_square

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.common_library.bean.DataX
import com.phone.common_library.service.ISquareService

@Route(path = "/module_square/SquareServiceImpl")
class SquareServiceImpl : ISquareService {

    override var mSquareDataList: List<DataX> = mutableListOf()
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }

}