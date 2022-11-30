package com.phone.library_common.adapter

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
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseRxFragment
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseRxFragment : RxFragment() {

    private val TAG = BaseRxFragment::class.java.simpleName
    protected var mRxAppCompatActivity: RxAppCompatActivity? = null
    protected var mBaseApplication: BaseApplication? = null

    protected var rootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(initLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRxAppCompatActivity = activity as RxAppCompatActivity?
        if (mRxAppCompatActivity != null) {
            mBaseApplication = mRxAppCompatActivity?.application as BaseApplication
        }
        initData()
        initViews()
        initLoadData()
    }

    protected abstract fun initLayoutId(): Int

    protected abstract fun initData()

    protected abstract fun initViews()

    protected abstract fun initLoadData()

    protected fun showToast(message: String?, isLongToast: Boolean) {
        //        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        mRxAppCompatActivity?.let {
            if (!it.isFinishing) {
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
        mRxAppCompatActivity?.let {
            val frameLayout = FrameLayout(it)
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            frameLayout.layoutParams = layoutParams
            val textView = TextView(it)
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
            val toast = Toast(it)
            toast.view = frameLayout
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }
    }

    fun isOnMainThread(): Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    protected fun startActivity(cls: Class<*>?) {
        val intent = Intent(mRxAppCompatActivity, cls)
        startActivity(intent)
    }

    protected fun startActivityCarryParams(cls: Class<*>?, params: Map<String?, String?>?) {
        val intent = Intent(mRxAppCompatActivity, cls)
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
        val intent = Intent(mRxAppCompatActivity, cls)
        startActivityForResult(intent, requestCode)
    }

    protected fun startActivityForResultCarryParams(
        cls: Class<*>?,
        params: Map<String?, String?>?,
        requestCode: Int
    ) {
        val intent = Intent(mRxAppCompatActivity, cls)
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

    protected fun isEmpty(dataStr: String?): Boolean {
        return if (dataStr != null && "" != dataStr) {
            false
        } else {
            true
        }
    }

    override fun onDestroyView() {
        if (mRxAppCompatActivity != null) {
            mRxAppCompatActivity = null
        }
        if (mBaseApplication != null) {
            mBaseApplication = null
        }
        if (rootView != null) {
            rootView = null
        }
        super.onDestroyView()
    }

}