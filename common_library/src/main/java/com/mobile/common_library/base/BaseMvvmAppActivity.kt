package com.mobile.common_library.base

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.mobile.common_library.BaseApplication
import com.mobile.common_library.R
import com.mobile.common_library.manager.ActivityPageManager
import com.mobile.common_library.manager.ScreenManager
import com.mobile.common_library.manager.ToolbarManager

/**
 * author    : xxxxxxxxxxx
 * e-mail    : 1164688204@qq.com
 * date      : 2020/10/15 9:15
 * introduce :
 */

abstract class BaseMvvmAppActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected var baseApplication: BaseApplication? = null

    //该类绑定的ViewDataBinding
    lateinit var mDatabind: DB
    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityPageManager.getInstance().addActivity(this)
        baseApplication = application as BaseApplication

        mDatabind = DataBindingUtil.setContentView(this, initLayoutId())
        mDatabind.lifecycleOwner = this

        viewModel = initViewModel()
        initData()
        initObservers()
        initViews()
        initLoadData()
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
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        frameLayout.layoutParams = layoutParams
        val textView = TextView(this)
        val layoutParams1 = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, ScreenManager.dipToPx(this, 40f))
        textView.layoutParams = layoutParams1
        textView.setPadding(ScreenManager.dipToPx(this, 20f), 0, ScreenManager.dipToPx(this, 20f), 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable()//创建drawable
        gradientDrawable.setColor(ContextCompat.getColor(this, R.color.color_FFE066FF))
        gradientDrawable.cornerRadius = ScreenManager.dipToPx(this, 20f).toFloat()
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

    protected fun showCustomToast(left: Int, right: Int,
                                  textSize: Int, textColor: Int,
                                  bgColor: Int, height: Int,
                                  roundRadius: Int, message: String,
                                  isLongToast: Boolean) {
        val frameLayout = FrameLayout(this)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
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

    override fun onDestroy() {
        mDatabind.unbind()
        viewModelStore.clear()
        ActivityPageManager.getInstance().removeActivity(this)
        super.onDestroy()
    }
}