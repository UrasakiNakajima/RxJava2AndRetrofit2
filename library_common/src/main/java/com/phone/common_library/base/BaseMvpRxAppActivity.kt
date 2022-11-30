package com.phone.common_library.base

import android.app.ActivityManager
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.os.Process
import android.util.ArrayMap
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.gyf.immersionbar.ImmersionBar
import com.phone.common_library.BaseApplication
import com.phone.common_library.R
import com.phone.common_library.manager.*
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseMvpRxAppActivity<V, T : BasePresenter<V>> : RxAppCompatActivity() {

    private val TAG = BaseMvpRxAppActivity::class.java.simpleName
    protected lateinit var mLoadView: QMUILoadingView
    protected lateinit var layoutParams: FrameLayout.LayoutParams

    protected lateinit var presenter: T
    protected var url: String? = null
    protected var mBodyParams = ArrayMap<String, String>()
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected var mBaseApplication: BaseApplication? = null
    private var mActivityPageManager: ActivityPageManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRxAppCompatActivity = this
        mBaseApplication = application as BaseApplication
        mActivityPageManager = ActivityPageManager.get()
        mActivityPageManager?.addActivity(this)
        setContentView(initLayoutId())

        //        setToolbar();
        presenter = attachPresenter()
        initData()
        initViews()
        mLoadView = QMUILoadingView(this)
        mLoadView.visibility = View.GONE
        mLoadView.setSize(100)
        mLoadView.setColor(ResourcesManager.getColor(R.color.color_333333))
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.gravity = Gravity.CENTER
        addContentView(mLoadView, layoutParams)
        initLoadData()

//        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance(this);
//        rxPermissionsManager.initRxPermissionsActivity(new OnCommonRxPermissionsCallback() {
//            @Override
//            public void onRxPermissionsAllPass() {
//                CrashHandlerManager crashHandlerManager = CrashHandlerManager.getInstance(mRxAppCompatActivity);
//                crashHandlerManager.sendPreviousReportsToServer();
//                crashHandlerManager.init();
//            }
//
//            @Override
//            public void onNotCheckNoMorePromptError() {
//
//            }
//
//            @Override
//            public void onCheckNoMorePromptError() {
//
//            }
//        });
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
                .statusBarColor(R.color.color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FF198CFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
        ToolbarManager.assistActivity(findViewById(R.id.content))
    }

    protected open fun setToolbar(isDarkFont: Boolean, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FFFFFFFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FF198CFF) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(R.id.content))
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
        ToolbarManager.assistActivity(findViewById(R.id.content))
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
            ToolbarManager.assistActivity(findViewById(R.id.content))
        }
    }

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract fun attachPresenter(): T

    protected open fun showToast(message: String?, isLongToast: Boolean) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if (!this.mRxAppCompatActivity.isFinishing) {
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
        toast.view = frameLayout
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

    protected fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    protected fun startActivityCarryParams(cls: Class<*>?, params: Map<String?, String?>?) {
        val intent = Intent(this, cls)
        val bundle = Bundle()
        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) { //如果参数不是null，才把参数传给后台
                    bundle.putString(key, params[key])
                }
            }
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(this, cls)
        startActivityForResult(intent, requestCode)
    }

    protected open fun startActivityForResultCarryParams(
        cls: Class<*>?,
        params: Map<String?, String?>?,
        requestCode: Int
    ) {
        val intent = Intent(this, cls)
        val bundle = Bundle()
        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) { //如果参数不是null，才把参数传给后台
                    bundle.putString(key, params[key])
                }
            }
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    protected fun detachPresenter() {
        presenter.detachView()
    }

    open fun getActivityPageManager(): ActivityPageManager? {
        return mActivityPageManager
    }

    private fun killAppProcess() {
        LogManager.i(TAG, "killAppProcess")
        mBaseApplication?.let {
            val manager = it.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val processInfos = manager.runningAppProcesses
            // 先杀掉相关进程，最后再杀掉主进程
            for (runningAppProcessInfo in processInfos) {
                if (runningAppProcessInfo.pid != Process.myPid()) {
                    Process.killProcess(runningAppProcessInfo.pid)
                }
            }
            LogManager.i(TAG, "执行killAppProcess，應用開始自殺")
            val crashHandlerManager = CrashHandlerManager.get()
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
    }

    override fun onDestroy() {
        detachPresenter()
        mBodyParams.clear()
        if (mActivityPageManager?.mIsLastAliveActivity?.get() == true) {
            killAppProcess()
        }
        mActivityPageManager?.removeActivity(mRxAppCompatActivity)
        super.onDestroy()
    }
}