package com.phone.library_common.bean

import java.io.Serializable

class ApiResponse2<T> : Serializable {

    var reason: String? = null
    var result: ResultData? = null
    var error_code = -1
    var error: Throwable? = null

}