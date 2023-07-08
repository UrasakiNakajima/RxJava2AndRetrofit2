package com.phone.module_project

import com.phone.library_base.BaseApplication

class ProjectApplication : BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Project"
        super.onCreate()
    }
}