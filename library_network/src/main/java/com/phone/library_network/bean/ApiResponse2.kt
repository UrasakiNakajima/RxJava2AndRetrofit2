package com.phone.library_network.bean

import com.phone.call_third_party_so.bean.ResultData
import java.io.Serializable

class ApiResponse2<T> : Serializable {

    var reason: String? = null
    var result: ResultData? = null
    var error_code = -1
    var error: Throwable? = null

}