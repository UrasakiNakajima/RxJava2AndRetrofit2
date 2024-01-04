package com.phone.library_base.base

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
import com.phone.library_base.BaseApplication
import com.phone.library_base.manager.DialogManager
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseRxFragment(contentLayoutId: Int) : RxFragment(contentLayoutId), IBaseView {

    private val TAG = BaseRxFragment::class.java.simpleName
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected lateinit var mRxFragment: RxFragment
    protected lateinit var mBaseApplication: BaseApplication
    protected var mRootView: View? = null

    protected val mDialogManager = DialogManager()
    protected var mIsLoadView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //        if (mRootView == null) {
        //            mRootView = inflater.inflate(initLayoutId(), container, false);
        //        } else {
        //            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
        //            if (viewGroup != null) {
        //                viewGroup.removeView(mRootView);
        //            }
        //        }

        mRxFragment = this
//        mRootView = inflater.inflate(initLayoutId(), container, false)
        mRootView = super.onCreateView(inflater, container, savedInstanceState)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRxAppCompatActivity = activity as RxAppCompatActivity
        mBaseApplication = mRxAppCompatActivity.application as BaseApplication
        initData()
        initViews()
    }

//    protected abstract fun initLayoutId(): Int

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    override fun onResume() {
        super.onResume()
        initLoadData()
    }

    override fun showLoading() {
        if (!mIsLoadView) {
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
        if (!mIsLoadView) {
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
        message?.let {
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
    }

    protected fun showCustomToast(
        left: Int, right: Int,
        textSize: Int, textColor: Int,
        bgColor: Int, height: Int,
        roundRadius: Int, message: String?
    ) {
        message?.let {
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
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    override fun onDestroyView() {
        if (mIsLoadView) {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissProgressBarDialog()
            }
        } else {
            if (!mRxAppCompatActivity.isFinishing) {
                mDialogManager.dismissLoadingDialog()
            }
        }
        if (mRootView != null) {
            mRootView = null
        }
        super.onDestroyView()
    }

}