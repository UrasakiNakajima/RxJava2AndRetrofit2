package com.phone.library_common.bean

class UserResponseListBean {

    var code: Int? = null
    var message: String? = null
    var userBeanList: List<UserResponse> = ArrayList()

    override fun toString(): String {
        return "UserResponseListBean(code=$code, message=$message, userBeanList=$userBeanList)"
    }

}