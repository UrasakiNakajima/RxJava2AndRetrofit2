package com.phone.module_square

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseBindingRxAppActivity
import com.phone.library_common.base.BaseMvpRxAppActivity
import com.phone.library_common.base.BaseMvvmAppRxActivity
import com.phone.library_common.base.State
import com.phone.library_common.bean.*
import com.phone.library_common.callback.OnCommonRxPermissionsCallback
import com.phone.library_common.common.ConstantData
import com.phone.library_common.manager.*
import com.phone.library_common.service.ISquareService
import com.phone.module_square.databinding.SquareActivitySquareBinding
import com.phone.module_square.view_model.SquareViewModelImpl
import java.util.concurrent.atomic.AtomicBoolean

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

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

}
