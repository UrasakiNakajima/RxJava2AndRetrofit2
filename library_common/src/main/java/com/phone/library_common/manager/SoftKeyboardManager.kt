package com.phone.library_common.manager

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.phone.library_base.callback.OnSoftKeyBoardChangeListener

class SoftKeyboardManager(activity: Activity) {

    private val TAG = SoftKeyboardManager::class.java.simpleName

    //activity的根视图
    private var rootView: View

    //纪录根视图的显示高度
    private var rootViewVisibleHeight = 0
    private var onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    init {
        //获取activity的根视图
        rootView = activity.window.decorView

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver()
            .addOnGlobalLayoutListener(OnGlobalLayoutListener { //获取当前根视图在屏幕上显示的大小
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)
                val visibleHeight = r.height()
                println("" + visibleHeight)
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    onSoftKeyBoardChangeListener?.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }

                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    onSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                    rootViewVisibleHeight = visibleHeight
                    return@OnGlobalLayoutListener
                }
            })
    }

    private fun setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
        this.onSoftKeyBoardChangeListener = onSoftKeyBoardChangeListener
    }

    fun setListener(
        activity: Activity,
        onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener
    ) {
        val softKeyBoardListener = SoftKeyboardManager(activity)
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener)
    }

    companion object {

        /**
         * 显示软键盘（输入法）（可用于Activity，Fragment）
         *
         * @param activity
         * @param editText
         */
        @JvmStatic
        fun showInputMethod(activity: Activity, editText: EditText) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }


        /**
         * 隐藏软键盘（输入法）（可用于Activity，Fragment）
         *
         * @param activity
         */
        @JvmStatic
        fun hideInputMethod(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive) {
                activity.currentFocus?.let {
                    if (it.windowToken != null) {
                        inputMethodManager.hideSoftInputFromWindow(
                            it.windowToken,
                            InputMethodManager.HIDE_NOT_ALWAYS
                        )
                    }
                }
            }
        }

        /**
         * 显示软键盘（输入法）（只适用于Activity，不适用于Fragment）
         */
        @JvmStatic
        fun showSoftKeyboard2(activity: Activity) {
            val view = activity.currentFocus
            view?.let {
                val inputMethodManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        /**
         * 隐藏软键盘（输入法）（只适用于Activity，不适用于Fragment）
         */
        @JvmStatic
        fun hideSoftKeyboard(activity: Activity) {
            val view = activity.currentFocus
            view?.let {
                val inputMethodManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }

        /**
         * 隐藏软键盘（输入法）（可用于Activity，Fragment）
         */
        @JvmStatic
        fun hideSoftKeyboard(context: Context, viewList: List<View>) {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            for (v in viewList) {
                inputMethodManager.hideSoftInputFromWindow(
                    v.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

}