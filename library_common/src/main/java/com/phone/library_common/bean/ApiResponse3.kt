package com.phone.library_common.bean

import java.io.Serializable

class ApiResponse3<T> : Serializable {

    var reason: String? = null
    var result: MineResult? = null
    var error_code = -1
    var error: Throwable? = null

}