package com.phone.common_library.base

import java.lang.ref.WeakReference

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */

open class BasePresenter<T> {

    private var modelView: WeakReference<T?>? = null

    protected fun attachView(view: T) {
        modelView = WeakReference(view)
    }

    protected fun obtainView(): T? {
        return if (isAttach()) modelView?.get() else null
    }

    private fun isAttach(): Boolean {
        return modelView != null &&
                modelView?.get() != null
    }

    open fun detachView() {
        if (isAttach()) {
            modelView?.clear()
            modelView = null
        }
    }

}