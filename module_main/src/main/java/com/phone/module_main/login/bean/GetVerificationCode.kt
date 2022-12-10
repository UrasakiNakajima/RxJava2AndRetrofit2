package com.phone.module_main.login

data class GetVerificationCode(

    var code: Int,
    var message: String,
    var data: DataGetVerification
)

class DataGetVerification(
    var content: String
)