package com.phone.library_common.base

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
import androidx.fragment.app.DialogFragment
import com.phone.library_common.BaseApplication
import com.phone.library_common.R
import com.phone.library_common.callback.OnDialogCallback
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseBindingDialogFragment<DB : ViewDataBinding> : DialogFragment() {

    private val TAG = BaseBindingRxFragment::class.java.simpleName

    //该类绑定的ViewDataBinding
    protected lateinit var mDatabind: DB
    protected var mRxAppCompatActivity: RxAppCompatActivity? = null
    protected var mBaseApplication: BaseApplication? = null

    protected var rootView: View? = null

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
        mRxAppCompatActivity = activity as RxAppCompatActivity?
        mBaseApplication = mRxAppCompatActivity?.application as BaseApplication
        initData()
        initViews()
        initLoadData()
    }

    override fun onStart() {
        super.onStart()
        // 下面这些设置必须在此方法(onStart())中才有效
        val window = dialog?.window
        // 如果不设置这句代码, 那么弹框就会与四边都有一定的距离
        //        window.setBackgroundDrawableResource(android.R.color.library_transparent);
        window?.let {
            window.setBackgroundDrawableResource(R.drawable.library_corners_14_color_white)
            // 设置动画
            //		window.setWindowAnimations(R.style.BottomDialogAnimation);
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
            params.width = resources.displayMetrics.widthPixels
            window.attributes = params
        }
    }

    private var onDialogCallback: OnDialogCallback<Int>? = null

    open fun setOnDialogCallback(onDialogCallback: OnDialogCallback<Int>?) {
        this.onDialogCallback = onDialogCallback
    }

    open fun getOnDialogCallback() = onDialogCallback

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
                toast = Toast.makeText(it, message, duration)
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
            bundle.let {
                intent.putExtras(it)
            }
        }
        startActivity(intent)
    }

    protected fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        val intent = Intent(mRxAppCompatActivity, cls)
        startActivityForResult(intent, requestCode)
    }

    override fun onDestroyView() {
        mRxAppCompatActivity = null
        mBaseApplication = null
        rootView = null
        super.onDestroyView()
    }

}