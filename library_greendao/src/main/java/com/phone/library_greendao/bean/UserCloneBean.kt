package com.phone.library_greendao.bean

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.phone.library_greendao.AddressBeanListConverter
import org.greenrobot.greendao.annotation.Convert
import java.math.BigDecimal

class UserCloneBean {

    var id: Long? = null
    var userId: String? = null
    var userName: String? = null
    var date: String? = null
    var age = 0
    var salary: Double? = null

    @JsonSerialize(using = ToStringSerializer::class)
    var salaryBigDecimal: BigDecimal? = null

    @Convert(columnType = String::class, converter = AddressBeanListConverter::class)
    var addressBeanList: List<AddressBean> = ArrayList()

}