package com.phone.square_module.observer

import com.phone.common_library.base.BaseRxAppActivity
import com.phone.common_library.manager.LogManager
import com.phone.square_module.R

class ObserverActivity : BaseRxAppActivity() {

    companion object {
        val TAG = ObserverActivity::class.java.simpleName
    }

    val mineObserver = MineObserver();
    val mineObserver2 = MineObserver2();
    val mineObserver3 = MineObserver3();

    val mineObservable = MineObservable();

    override fun initLayoutId(): Int {
        return R.layout.activity_observer
    }

    override fun initData() {
        mineObservable.addObserver(mineObserver)
        mineObservable.addObserver(mineObserver2)
        mineObservable.addObserver(mineObserver3)
    }

    override fun initViews() {

    }

    override fun initLoadData() {
        LogManager.i(TAG, "start send data")
        mineObservable.sendData("This is the data");
    }


}