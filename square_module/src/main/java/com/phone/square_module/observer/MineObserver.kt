package com.phone.square_module.observer

import com.phone.common_library.manager.LogManager
import java.util.*

class MineObserver : Observer {

    companion object {
        val TAG = MineObserver2::class.java.simpleName
    }

    override fun update(o: Observable?, arg: Any?) {

        LogManager.i(TAG, "arg*****" + arg)
    }

}