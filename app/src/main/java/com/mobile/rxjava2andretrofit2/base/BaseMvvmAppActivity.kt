package com.mobile.rxjava2andretrofit2.base

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.manager.ActivityPageManager

abstract class BaseMvvmAppActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected var mineApplication: MineApplication? = null
    //该类绑定的ViewDataBinding
    lateinit var mDatabind: DB
    var viewModel: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityPageManager.getInstance().addActivity(this)
        mineApplication = application as MineApplication

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

    protected abstract fun initData()

    protected abstract fun initObservers()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    protected fun showCustomToast(left: Int, right: Int,
                                  textSize: Int, textColor: Int,
                                  bgColor: Int, height: Int,
                                  roundRadius: Int, message: String) {
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
        toast.duration = Toast.LENGTH_LONG
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