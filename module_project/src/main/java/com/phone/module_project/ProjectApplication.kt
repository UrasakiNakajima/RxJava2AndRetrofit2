package com.phone.module_project

import com.phone.library_common.BaseApplication

class ProjectApplication :BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Project"
        super.onCreate()
    }
}