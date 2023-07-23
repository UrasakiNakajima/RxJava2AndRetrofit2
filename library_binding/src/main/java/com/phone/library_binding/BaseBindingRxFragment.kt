package com.phone.library_binding

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.phone.library_base.BaseApplication
import com.phone.library_base.base.IBaseView
import com.phone.library_base.manager.DialogManager
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseBindingRxFragment<DB : ViewDataBinding>() : RxFragment(), IBaseView {

    private val TAG = BaseBindingRxFragment::class.java.simpleName

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected var mBaseApplication: BaseApplication? = null
    protected var mRootView: View? = null
    protected val mDialogManager = DialogManager()
    protected var mIsLoadView = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDatabind = DataBindingUtil.inflate(inflater, initLayoutId(), container, false)
        mDatabind.lifecycleOwner = viewLifecycleOwner
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRxAppCompatActivity = activity as RxAppCompatActivity
        mBaseApplication = mRxAppCompatActivity.application as BaseApplication
        initData()
        initViews()
        initLoadData()
    }

    protected abstract fun initLayoutId(): Int

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

    fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    override fun onDestroy() {
        mDatabind.unbind()
        viewModelStore.clear()
        if (mIsLoadView) {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissProgressBarDialog()
            }
        } else {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissLoadingDialog()
            }
        }
        mBaseApplication = null
        mRootView = null
        super.onDestroy()
    }

}