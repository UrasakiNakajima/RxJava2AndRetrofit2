package com.phone.library_common.base

sealed class State<T>{

    class SuccessState<T>(val success: T) : State<T>()

    class ErrorState<T>(val errorMsg: String) : State<T>()
}


