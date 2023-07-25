package com.phone.module_square

import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.ThreadPoolManager
import com.phone.library_room.AppRoomDataBase
import com.phone.library_room.Book

class SquareApplication : BaseApplication() {

    override fun onCreate() {
        mWhichPage = "Square"

        super.onCreate()
    }
}