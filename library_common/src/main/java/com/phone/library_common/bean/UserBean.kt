package com.phone.library_common.bean

import org.greenrobot.greendao.annotation.Convert
import org.greenrobot.greendao.annotation.Entity
import org.greenrobot.greendao.annotation.Generated
import org.greenrobot.greendao.annotation.Id

@Entity
open class UserBean : Cloneable {

    @Id(autoincrement = true)
    var id: Long? = null
    var userId: String? = null
    var password: String? = null
    var birthday: String? = null
    var age: Int? = null
    var salary: Double? = null

    @Convert(columnType = String::class, converter = AddressBeanListConverter::class)
    var addressBeanList: List<AddressBean> = ArrayList()

    @Generated(hash = 703739529)
    constructor(
        id: Long?, userId: String?, password: String?, birthday: String?,
        age: Int?, salary: Double?, addressBeanList: List<AddressBean>
    ) {
        this.id = id
        this.userId = userId
        this.password = password
        this.birthday = birthday
        this.age = age
        this.salary = salary
        this.addressBeanList = addressBeanList
    }

    @Generated(hash = 1203313951)
    constructor() {
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): UserBean {
        val addressBeanList: MutableList<AddressBean> = ArrayList()
        val userBean = super.clone() as UserBean
        for (i in userBean.addressBeanList.indices) {
            addressBeanList.add(userBean.addressBeanList[i].clone())
        }
        userBean.addressBeanList = addressBeanList
        return userBean
    }

}