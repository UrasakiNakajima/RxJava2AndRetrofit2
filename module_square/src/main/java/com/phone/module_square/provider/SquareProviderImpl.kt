package com.phone.module_square.provider

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.phone.library_common.bean.SubDataSquare
import com.phone.library_common.common.ConstantData
import com.phone.library_common.service.ISquareProvider

@Route(path = ConstantData.Route.ROUTE_SQUARE_SERVICE)
class SquareProviderImpl : ISquareProvider {

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