package com.phone.module_main.login

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