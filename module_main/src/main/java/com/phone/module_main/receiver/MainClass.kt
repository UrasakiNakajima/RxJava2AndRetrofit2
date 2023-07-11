package com.phone.module_main.receiver

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