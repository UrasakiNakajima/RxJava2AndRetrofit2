package com.phone.rxjava2andretrofit2

import com.phone.library_base.manager.LogManager

/**
 * 这里只是kotlin写的main方法，主要用来临时测试某些功能（最近升级Android Gradle Plugin和jdk版本了，影响了main方法运行，先不要使用了）
 */
class KotlinMainClass {

    companion object {

        private val TAG = KotlinMainClass::class.java.simpleName

        @JvmStatic
        fun main(args: Array<String>) {
            LogManager.i(TAG, "MineClass main函数")
        }
    }
}