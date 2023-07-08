package com.phone.module_mine

import com.phone.library_base.BaseApplication

class MineApplication : BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Mine"
        super.onCreate()
    }
}