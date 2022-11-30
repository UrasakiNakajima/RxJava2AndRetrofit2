package com.phone.module_square.observer

import com.phone.library_common.manager.LogManager
import java.util.*

class MineObserver2 : Observer {

    companion object {
        val TAG = MineObserver::class.java.simpleName
    }

    override fun update(o: Observable?, arg: Any?) {

        LogManager.i(TAG, "arg*****" + arg)
    }
}