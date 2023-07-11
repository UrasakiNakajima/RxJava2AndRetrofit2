package com.phone.library_mvvm

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
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.phone.library_base.BaseApplication
import com.phone.library_base.base.BaseRxAppActivity
import com.phone.library_base.manager.ActivityPageManager
import com.phone.library_base.manager.CrashHandlerManager
import com.phone.library_base.manager.LogManager
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.ScreenManager
import com.phone.library_base.manager.ToolbarManager
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseMvvmAppRxActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseRxAppActivity() {

    companion object {
        private val TAG = BaseMvvmAppRxActivity::class.java.simpleName
    }

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        mDatabind = DataBindingUtil.setContentView(mRxAppCompatActivity, initLayoutId())
        mDatabind.lifecycleOwner = mRxAppCompatActivity
        viewModel = initViewModel()
        super.onCreate(savedInstanceState)
    }

    protected abstract fun initViewModel(): VM

    protected abstract fun initObservers()

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
            ScreenManager.dpToPx(40f)
        )
        textView.layoutParams = layoutParams1
        textView.setPadding(ScreenManager.dpToPx(20f), 0, ScreenManager.dpToPx(20f), 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.toFloat())
        textView.setTextColor(ResourcesManager.getColor(R.color.library_white))
        textView.gravity = Gravity.CENTER
        textView.includeFontPadding = false
        val gradientDrawable = GradientDrawable()//创建drawable
        gradientDrawable.setColor(ResourcesManager.getColor(R.color.library_color_989898))
        gradientDrawable.cornerRadius = ScreenManager.dpToPx(20f).toFloat()
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

    override fun onDestroy() {
        mDatabind.unbind()
        viewModelStore.clear()
        super.onDestroy()
    }

}