package com.phone.library_network.bean

import com.phone.call_third_party_so.bean.MineResult
import java.io.Serializable

class ApiResponse3<T> : Serializable {

    var reason: String? = null
    var result: MineResult? = null
    var error_code = -1
    var error: Throwable? = null

}