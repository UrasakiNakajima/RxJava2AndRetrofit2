package com.phone.module_home

import com.phone.library_base.BaseApplication

class HomeApplication : BaseApplication() {

    companion object {
        private val TAG = "HomeApplication"
    }

    override fun onCreate() {
        mWhichPage = "Home"
        super.onCreate()
    }

}