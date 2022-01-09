package com.phone.common_library.base

import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseMvvmRxFragment<VM : BaseViewModel, DB : ViewDataBinding> : RxFragment() {

    //该类绑定的ViewDataBinding
    lateinit var mDatabind: DB
    var viewModel: VM? = null
    protected var rxAppCompatActivity: RxAppCompatActivity? = null
    protected var intent: Intent? = null
    protected var bundle: Bundle? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDatabind = DataBindingUtil.inflate(inflater, initLayoutId(), container, false)
        mDatabind.lifecycleOwner = viewLifecycleOwner
        return mDatabind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rxAppCompatActivity = getActivity() as RxAppCompatActivity?;
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
        val frameLayout = FrameLayout(rxAppCompatActivity!!)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        frameLayout.layoutParams = layoutParams
        val textView = TextView(rxAppCompatActivity)
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

        val toast = Toast(rxAppCompatActivity)
        toast.view = frameLayout
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    protected fun startActivity(cls: Class<*>) {
        intent = Intent(rxAppCompatActivity, cls)
        startActivity(intent)
    }

    protected fun startActivityCarryParams(cls: Class<*>, params: Map<String, String>?) {
        intent = Intent(rxAppCompatActivity, cls)
        bundle = Bundle()

        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) {//如果参数不是null，才把参数传给后台
                    bundle!!.putString(key, params[key])
                }
            }
            intent!!.putExtras(bundle!!)
        }
        startActivity(intent)
    }

    protected fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        intent = Intent(rxAppCompatActivity, cls)
        startActivityForResult(intent, requestCode)
    }

    protected fun startActivityForResultCarryParams(cls: Class<*>, params: Map<String, String>?, requestCode: Int) {
        intent = Intent(rxAppCompatActivity, cls)
        bundle = Bundle()

        if (params != null && params.size > 0) {
            for (key in params.keys) {
                if (params[key] != null) {//如果参数不是null，才把参数传给后台
                    bundle!!.putString(key, params[key])
                }
            }
            intent!!.putExtras(bundle!!)
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
        mDatabind.unbind()
        viewModelStore.clear()
        viewModel = null
        super.onDestroyView()
    }
}