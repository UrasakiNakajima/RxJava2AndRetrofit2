package com.phone.library_mvvm

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
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

abstract class BaseMvvmRxFragment<VM : BaseViewModel, DB : ViewDataBinding> : RxFragment(),
    IBaseView {

    companion object {
        private val TAG = BaseMvvmRxFragment::class.java.simpleName
    }

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected lateinit var mViewModel: VM
    protected lateinit var mRxAppCompatActivity: RxAppCompatActivity
    protected lateinit var mBaseApplication: BaseApplication

    protected val mDialogManager = DialogManager()
    protected var mIsLoadView = true

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
        mViewModel = initViewModel()
        initData()
        initObservers()
        initViews()
        mDatabind.root
        initLoadData()
    }

    protected abstract fun initLayoutId(): Int

    protected abstract fun initViewModel(): VM

    protected abstract fun initData()

    protected abstract fun initObservers()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

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

    override fun onDestroy() {
        mDatabind.unbind()
        viewModelStore.clear()
        super.onDestroy()
    }
}