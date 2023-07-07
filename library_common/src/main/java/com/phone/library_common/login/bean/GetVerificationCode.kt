package com.phone.library_common.login.bean

data class GetVerificationCode(

    var code: Int,
    var message: String,
    var data: DataGetVerification
)

class DataGetVerification(
    var content: String
)