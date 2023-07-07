package com.phone.module_mine

import com.phone.library_common.BaseApplication

class MineApplication : BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Mine"
        super.onCreate()
    }
}