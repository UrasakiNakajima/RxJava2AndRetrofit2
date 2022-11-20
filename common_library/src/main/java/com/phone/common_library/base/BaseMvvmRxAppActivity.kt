package com.phone.common_library.base

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Process
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.phone.common_library.BaseApplication
import com.phone.common_library.R
import com.phone.common_library.manager.*
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseMvvmAppRxActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    RxAppCompatActivity() {

    companion object {
        private val TAG = BaseMvvmAppRxActivity::class.java.simpleName
    }

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var viewModel: VM
    protected lateinit var rxAppCompatActivity: RxAppCompatActivity
    protected lateinit var baseApplication: BaseApplication
    private lateinit var activityPageManager: ActivityPageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rxAppCompatActivity = this;
        activityPageManager = ActivityPageManager.getInstance();
        activityPageManager.addActivity(rxAppCompatActivity)
        baseApplication = application as BaseApplication

        mDatabind = DataBindingUtil.setContentView(rxAppCompatActivity, initLayoutId())
        mDatabind.lifecycleOwner = rxAppCompatActivity

        viewModel = initViewModel()
        initData()
        initObservers()
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

    protected abstract fun initViewModel(): VM

    protected fun setToolbar(isDarkFont: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FF198CFF)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        }

        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected fun setToolbar(isDarkFont: Boolean, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FFFFFFFF)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .keyboardEnable(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(R.color.color_FF198CFF)     //状态栏颜色，不写默认透明色
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
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }

        ToolbarManager.assistActivity(findViewById(android.R.id.content))
    }

    protected fun setToolbar(isDarkFont: Boolean, color: Int, isResizeChildOfContent: Boolean) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
                .statusBarColor(color)     //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }

        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content))
        }
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected fun initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
            .navigationBarColor(R.color.color_FFE066FF).init()
    }

    protected abstract fun initData()

    protected abstract fun initObservers()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    protected fun showCustomToast(message: String, isLongToast: Boolean) {
        val frameLayout = FrameLayout(this)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = layoutParams
        val textView = TextView(this)
        val layoutParams1 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            ScreenManager.dpToPx(this, 40f)
        )
        textView.layoutParams = layoutParams1
        textView.setPadding(ScreenManager.dpToPx(this, 20f), 0, ScreenManager.dpToPx(this, 20f), 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable()//创建drawable
        gradientDrawable.setColor(ContextCompat.getColor(this, R.color.color_FFE066FF))
        gradientDrawable.cornerRadius = ScreenManager.dpToPx(this, 20f).toFloat()
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
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    protected fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String,
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
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        textView.setTextColor(textColor)
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable()//创建drawable
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
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    protected open fun startActivity(cls: Class<*>?) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    open fun getActivityPageManager2(): ActivityPageManager? {
        return activityPageManager
    }

    private fun killAppProcess(context: Context) {
        LogManager.i(TAG, "killAppProcess")
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = manager.runningAppProcesses
        // 先杀掉相关进程，最后再杀掉主进程
        for (runningAppProcessInfo in processInfos) {
            if (runningAppProcessInfo.pid != Process.myPid()) {
                Process.killProcess(runningAppProcessInfo.pid)
            }
        }

        LogManager.i(TAG, "执行killAppProcess，應用開始自殺")
        val crashHandlerManager = CrashHandlerManager.getInstance(context)
        crashHandlerManager.saveTrimMemoryInfoToFile("执行killAppProcess，應用開始自殺")
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
        mDatabind.unbind()
        viewModelStore.clear()
        if (activityPageManager.isLastAliveActivity.get()) {
            killAppProcess(baseApplication)
        }
        activityPageManager.removeActivity(rxAppCompatActivity)
        super.onDestroy()
    }

}