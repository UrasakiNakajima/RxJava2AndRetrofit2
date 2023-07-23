package com.phone.module_square.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_binding.BaseBindingRxAppActivity
import com.phone.library_base.common.ConstantData
import com.phone.module_square.R
import com.phone.module_square.databinding.SquareActivitySquareBinding

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      :
 * introduce :
 */
@Route(path = ConstantData.Route.ROUTE_SQUARE)
class SquareActivity :
    BaseBindingRxAppActivity<SquareActivitySquareBinding>() {

    companion object {
        private val TAG: String = SquareActivity::class.java.simpleName
    }

    override fun initLayoutId() = R.layout.square_activity_square

    override fun initData() {

    }

    override fun initViews() {
        ImmersionBar.with(mRxAppCompatActivity)
            .keyboardEnable(false)
            .statusBarDarkFont(false)
            .statusBarColor(R.color.library_color_FF198CFF)
            .navigationBarColor(R.color.library_color_FF198CFF).init()
    }

    override fun initLoadData() {

    }

//    private fun startAsyncTask() {
//        // This async task is an anonymous class and therefore has a hidden reference to the outer
//        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
//        // the activity instance will leak.
//        object : AsyncTask<Void?, Void?, Void?>() {
//            override fun doInBackground(vararg p0: Void?): Void? {
//                // Do some slow work in background
//                SystemClock.sleep(10000)
//                return null
//            }
//        }.execute()
//        Toast.makeText(mRxAppCompatActivity, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show()
//    }

}
