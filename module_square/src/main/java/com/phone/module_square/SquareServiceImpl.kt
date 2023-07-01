package com.phone.module_square

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.bean.SubDataSquare
import com.phone.library_common.common.ConstantData
import com.phone.library_common.service.ISquareService

@Route(path = ConstantData.Route.ROUTE_SQUARE_SERVICE)
class SquareServiceImpl : ISquareService {

    override var mSquareDataList: List<SubDataSquare> = mutableListOf()
        get() {
            return field
        }
        set(value) {
            field = value
        }

    override fun init(context: Context?) {
    }

}