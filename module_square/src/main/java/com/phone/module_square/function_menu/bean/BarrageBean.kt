package com.phone.module_square.function_menu.bean

class BarrageBean {

    var url: String? = null
    var nickName: String? = null
    var oneself: Boolean = false
    var width = 0

    constructor(url: String?, nickName: String?) {
        this.url = url
        this.nickName = nickName
    }

    override fun toString(): String {
        return "BarrageBean(url='$url', nickName='$nickName')"
    }

}