package com.phone.library_base.base

import android.app.ActivityManager
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.os.Process
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_base.manager.ActivityPageManager
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.CrashHandlerManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.R
import com.phone.library_base.manager.DialogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ToolbarManager
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseRxAppActivity : RxAppCompatActivity(), IBaseView {

    private val TAG = BaseRxAppActivity::class.java.simpleName

    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected lateinit var mBaseApplication: BaseApplication
    var mActivityPageManager: ActivityPageManager? = null
    private val mDialogManager = DialogManager()
    protected var mIsLoadView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRxAppCompatActivity = this
        mBaseApplication = application as BaseApplication
        mActivityPageManager = ActivityPageManager.instance()
        mActivityPageManager?.addActivity(mRxAppCompatActivity)
        setContentView(initLayoutId())
        initData()
        initViews()
        initLoadData()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        //非默认值
        if (newConfig.fontScale != 1f) {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? { //还原字体大小
        val res = super.getResources()
        //非默认值
        if (res.configuration.fontScale != 1f) {
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    protected abstract fun initLayoutId(): Int

    protected fun setToolbar(isDarkFont: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(mRxAppCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(mRxAppCompatActivity)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FF198CFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected fun setToolbar(isDarkFont: Boolean, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(mRxAppCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(mRxAppCompatActivity)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FF198CFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content))
        }
    }

    protected fun setToolbar(isDarkFont: Boolean, color: Int) {
        if (isDarkFont) {
            ImmersionBar.with(mRxAppCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(mRxAppCompatActivity)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected fun setToolbar(isDarkFont: Boolean, color: Int, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(mRxAppCompatActivity) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(mRxAppCompatActivity)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content))
        }
    }

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    override fun showLoading() {
        if (mIsLoadView) {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.showProgressBarDialog(mRxAppCompatActivity)
            }
        } else {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.showLoadingDialog(mRxAppCompatActivity)
            }
        }
    }

    override fun hideLoading() {
        if (mIsLoadView) {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissProgressBarDialog()
            }
        } else {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissLoadingDialog()
            }
        }
    }

    protected fun showToast(message: String?, isLongToast: Boolean) {
        //        Toast.makeText(mRxAppCompatActivity, message, Toast.LENGTH_LONG).show();
        if (!mRxAppCompatActivity.isFinishing) {
            val toast: Toast
            val duration: Int
            duration = if (isLongToast) {
                Toast.LENGTH_LONG
            } else {
                Toast.LENGTH_SHORT
            }
            toast = Toast.makeText(mRxAppCompatActivity, message, duration)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    protected fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String?
    ) {
        val frameLayout = FrameLayout(mRxAppCompatActivity)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = layoutParams
        val textView = TextView(mRxAppCompatActivity)
        val layoutParams1 = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height)
        textView.layoutParams = layoutParams1
        textView.setPadding(left, 0, right, 0)
        textView.textSize = textSize.toFloat()
        textView.setTextColor(textColor)
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable() //创建drawable
        gradientDrawable.setColor(bgColor)
        gradientDrawable.cornerRadius = roundRadius.toFloat()
        textView.background = gradientDrawable
        textView.text = message
        frameLayout.addView(textView)
        val toast = Toast(mRxAppCompatActivity)
        toast.view = frameLayout
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    protected fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    private fun killAppProcess() {
        LogManager.i(TAG, "killAppProcess")
        val manager = mBaseApplication.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processInfos = manager.runningAppProcesses
        // 先杀掉相关进程，最后再杀掉主进程
        for (runningAppProcessInfo in processInfos) {
            if (runningAppProcessInfo.pid != Process.myPid()) {
                Process.killProcess(runningAppProcessInfo.pid)
            }
        }
        LogManager.i(TAG, "执行killAppProcess，應用開始自殺")
        val crashHandlerManager = CrashHandlerManager.instance()
        crashHandlerManager?.saveTrimMemoryInfoToFile("执行killAppProcess，應用開始自殺")
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            LogManager.i(TAG, "error")
        }
        Process.killProcess(Process.myPid())
        // 正常退出程序，也就是结束当前正在运行的 java 虚拟机
        System.exit(0)
    }

    override fun onDestroy() {
        if (mActivityPageManager != null) {
            if (mActivityPageManager?.mIsLastAliveActivity?.get() == true) {
                killAppProcess()
            }
            mActivityPageManager?.removeActivity(mRxAppCompatActivity)
        }
        super.onDestroy()
    }

}