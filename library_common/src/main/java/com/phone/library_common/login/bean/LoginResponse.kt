package com.phone.library_common.login.bean

data class LoginResponse(
    var code: Int,
    val message: String,
    val data: DataLogin
)

data class DataLogin(
    var validTime: Int,
    val userId: String,
    val token: String
)