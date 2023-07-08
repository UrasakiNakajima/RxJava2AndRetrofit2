package com.phone.library_base.manager

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.phone.library_base.callback.OnSoftKeyBoardChangeListener

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/26 11:12
 * introduce :
 */

class ToolbarManager {

    private val TAG = ToolbarManager::class.java.simpleName

    //activity的根视图
    private var rootView: View? = null

    //纪录根视图的显示高度
    private var rootViewVisibleHeight = 0
    private val onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    companion object {
        @JvmStatic
        fun assistActivity(content: View) {
            ToolbarManager(content)
        }

        @JvmStatic
        fun assistActivity(
            content: View,
            activity: Activity,
            onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener
        ) {
            ToolbarManager(
                content,
                activity,
                onSoftKeyBoardChangeListener
            )
        }

    }

    private var mChildOfContent: View
    private var usableHeightPrevious = 0
    private var frameLayoutParams: ViewGroup.LayoutParams

    constructor(content: View) {
        mChildOfContent = content
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
        frameLayoutParams = mChildOfContent.layoutParams
    }

    constructor(
        content: View,
        activity: Activity,
        onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener
    ) {
        //获取activity的根视图
        rootView = activity.window.decorView
        mChildOfContent = content
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent(
                onSoftKeyBoardChangeListener
            )
        }
        frameLayoutParams = mChildOfContent.layoutParams
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun possiblyResizeChildOfContent(onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener?) {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            frameLayoutParams.height = usableHeightNow
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }

        //获取当前根视图在屏幕上显示的大小
        val r = Rect()
        rootView?.getWindowVisibleDisplayFrame(r)
        val visibleHeight = r.height()
        println("" + visibleHeight)
        if (rootViewVisibleHeight == 0) {
            rootViewVisibleHeight = visibleHeight
            return
        }

        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewVisibleHeight == visibleHeight) {
            return
        }

        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewVisibleHeight - visibleHeight > 200) {
            onSoftKeyBoardChangeListener?.keyBoardShow(rootViewVisibleHeight - visibleHeight)
            rootViewVisibleHeight = visibleHeight
            return
        }

        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewVisibleHeight > 200) {
            onSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - rootViewVisibleHeight)
            rootViewVisibleHeight = visibleHeight
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

}