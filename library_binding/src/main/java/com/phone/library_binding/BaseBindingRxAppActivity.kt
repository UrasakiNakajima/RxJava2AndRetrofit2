package com.phone.library_binding

import android.app.ActivityManager
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.os.Process
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.ActivityPageManager
import com.phone.library_base.manager.CrashHandlerManager
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ToolbarManager
import com.phone.library_base.manager.ToolbarManager.Companion.assistActivity
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseBindingRxAppActivity<DB : ViewDataBinding> : RxAppCompatActivity(),
    IBaseView {

    private val TAG = BaseBindingRxAppActivity::class.java.simpleName
    protected lateinit var mLoadView: QMUILoadingView
    protected lateinit var layoutParams: FrameLayout.LayoutParams

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected lateinit var mBaseApplication: BaseApplication
    private var mActivityPageManager: ActivityPageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRxAppCompatActivity = this
        mBaseApplication = application as BaseApplication
        mActivityPageManager = ActivityPageManager.instance()
        mActivityPageManager?.addActivity(this)

        mDatabind = DataBindingUtil.setContentView(this, initLayoutId())
        mDatabind.lifecycleOwner = mRxAppCompatActivity
        initData()
        initViews()
        mLoadView = QMUILoadingView(this)
        mLoadView.also {
            it.visibility = View.GONE
            it.setSize(100)
            it.setColor(ResourcesManager.getColor(R.color.base_color_333333))
        }
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.gravity = Gravity.CENTER
        addContentView(mLoadView, layoutParams)
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

    protected open fun setToolbar(isDarkFont: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FF198CFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected open fun setToolbar(isDarkFont: Boolean, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.base_color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
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

    protected open fun setToolbar(isDarkFont: Boolean, color: Int) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected open fun setToolbar(
        isDarkFont: Boolean,
        color: Int,
        isResizeChildOfContent: Boolean
    ) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content))
        }
    }

    protected open fun setToolbar2(isDarkFont: Boolean, statusBarColor: Int) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
    }

    /**
     * 顶部栏吸顶专用
     */
    protected open fun setToolbar2(
        isDarkFont: Boolean,
        statusBarColor: Int,
        isResizeChildOfContent: Boolean
    ) {
        if (isDarkFont) {
            val window = window
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //        window.setNavigationBarColor(Color.TRANSPARENT);
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(statusBarColor) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
            val window = window
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //        window.setNavigationBarColor(Color.TRANSPARENT);
        }
        if (isResizeChildOfContent) {
            assistActivity(findViewById(android.R.id.content))
        }
    }

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    protected open fun showToast(message: String?, isLongToast: Boolean) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

    protected open fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String?,
        isLongToast: Boolean
    ) {
        val frameLayout = FrameLayout(this)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = layoutParams
        val textView = TextView(this)
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
        val toast = Toast(this)
        toast.setView(frameLayout)
        if (isLongToast) {
            toast.duration = Toast.LENGTH_LONG
        } else {
            toast.duration = Toast.LENGTH_SHORT
        }
        toast.show()
    }

    open fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    protected open fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    protected open fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(this, cls)
        startActivityForResult(intent, requestCode)
    }

    open fun getActivityPageManager(): ActivityPageManager? {
        return mActivityPageManager
    }

    private fun killAppProcess() {
        LogManager.i(TAG, "killAppProcess")
        val manager =
            mBaseApplication.getSystemService(ACTIVITY_SERVICE) as ActivityManager
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
        if (mActivityPageManager?.mIsLastAliveActivity?.get() == true) {
            killAppProcess()
        }
        mActivityPageManager?.removeActivity(mRxAppCompatActivity)
        super.onDestroy()
    }

}