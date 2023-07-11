package com.phone.library_mvp

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
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
import androidx.databinding.DataBindingUtil.setContentView
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_base.BaseApplication
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.manager.ActivityPageManager
import com.phone.library_base.manager.CrashHandlerManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ToolbarManager
import com.qmuiteam.qmui.widget.QMUILoadingView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseMvpRxAppActivity<V, T : BasePresenter<V>> : BaseRxAppActivity() {

    private val TAG = BaseMvpRxAppActivity::class.java.simpleName

    protected lateinit var presenter: T
    protected var mBodyParams = ArrayMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = attachPresenter()
        super.onCreate(savedInstanceState)
    }

    protected open fun setToolbarNew(
        isDarkFont: Boolean,
        isResizeChildOfContent: Boolean
    ) {
        if (isDarkFont) {
            ImmersionBar.with(this) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .statusBarDarkFont(isDarkFont)
//                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(isDarkFont)
//                .statusBarColor(color) //状态栏颜色，不写默认透明色
                //                    .autoStatusBarDarkModeEnable(true, 0.2f) //自动状态栏字体变色，必须指定状态栏颜色才可以自动变色哦
                .init()
        }
        if (isResizeChildOfContent) {
            ToolbarManager.assistActivity(findViewById(android.R.id.content))
        }
    }

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract fun attachPresenter(): T

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

    protected fun detachPresenter() {
        presenter.detachView()
    }

    override fun onDestroy() {
        detachPresenter()
        mBodyParams.clear()
        super.onDestroy()
    }
}