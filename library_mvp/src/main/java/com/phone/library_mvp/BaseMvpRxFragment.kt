package com.phone.library_mvp

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.util.ArrayMap
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.phone.library_base.BaseApplication
import com.phone.library_base.base.BaseRxFragment
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseMvpRxFragment<V, T : BasePresenter<V>> : BaseRxFragment() {

    private val TAG = BaseMvpRxFragment::class.java.simpleName
    protected var presenter: T? = null

    protected var url: String? = null
    protected var mBodyParams = ArrayMap<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = attachPresenter()
        super.onViewCreated(view, savedInstanceState)

//        RxPermissionsManager rxPermissionsManager = RxPermissionsManager.getInstance(this);
//        rxPermissionsManager.initRxPermissionsRxFragment(new OnCommonRxPermissionsCallback() {
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

    /**
     * 适配为不同的view 装载不同的presenter
     *
     * @return
     */
    protected abstract fun attachPresenter(): T

    protected fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String?,
        isLongToast: Boolean
    ) {
        val frameLayout = FrameLayout(mRxAppCompatActivity)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = layoutParams
        val textView = TextView(mRxAppCompatActivity)
        val layoutParams1 =
            FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height)
        textView.layoutParams = layoutParams1
        textView.setPadding(left, 0, right, 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
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
        if (isLongToast) {
            toast.duration = Toast.LENGTH_LONG
        } else {
            toast.duration = Toast.LENGTH_SHORT
        }
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    protected fun detachPresenter() {
        presenter?.detachView()
    }

    override fun onDestroyView() {
        detachPresenter()
        mBodyParams.clear()
        rootView?.let {
            rootView = null
        }
        super.onDestroyView()
    }

}
