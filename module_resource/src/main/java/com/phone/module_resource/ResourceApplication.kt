package com.phone.module_resource

import com.phone.library_common.BaseApplication

class ResourceApplication:BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Resource"
        super.onCreate()
    }
}