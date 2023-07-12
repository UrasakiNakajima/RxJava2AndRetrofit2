package com.phone.rxjava2andretrofit2.receiver

import com.phone.library_base.manager.LogManager

class MainClass {

    companion object {

        private val TAG = MainClass::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            LogManager.i(TAG, "MineClass main函数")
        }
    }
}