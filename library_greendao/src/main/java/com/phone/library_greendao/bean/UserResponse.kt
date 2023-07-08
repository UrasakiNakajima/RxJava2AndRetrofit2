package com.phone.library_greendao.bean

import com.phone.library_greendao.AddressBeanListConverter
import org.greenrobot.greendao.annotation.Convert

class UserResponse : Cloneable {

    var id: Long? = null
    var userId: String? = null
    var userName: String? = null
    var date: String? = null
    var age = 0
    var salary: String? = null

    @Convert(columnType = String::class, converter = AddressBeanListConverter::class)
    var addressBeanList: List<AddressBean> = ArrayList()

    @Throws(CloneNotSupportedException::class)
    override fun clone(): UserResponse {
        val addressBeanList: MutableList<AddressBean> = ArrayList()
        val userResponse = super.clone() as UserResponse
        for (i in userResponse.addressBeanList.indices) {
            addressBeanList.add(userResponse.addressBeanList[i].clone())
        }
        userResponse.addressBeanList = addressBeanList
        return userResponse
    }

    override fun toString(): String {
        return "UserResponse(id=$id, userId=$userId, userName=$userName, date=$date, age=$age, salary=$salary, addressBeanList=$addressBeanList)"
    }

}