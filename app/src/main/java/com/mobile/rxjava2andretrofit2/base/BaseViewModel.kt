package com.mobile.rxjava2andretrofit2.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.ArrayList

open class BaseViewModel : ViewModel() {

    private var modelView: WeakReference<IBaseView>? = null
    protected var disposableList: MutableList<Disposable>? = null
    protected var disposable: Disposable? = null

    protected fun attachView(view: IBaseView) {
        modelView = WeakReference<IBaseView>(view)
        disposableList = ArrayList<Disposable>()
    }

    protected fun obtainView(): IBaseView? {
        return if (isAttach()) modelView!!.get() else null
    }

    private fun isAttach(): Boolean {
        return modelView != null && modelView!!.get() != null
    }

    protected fun detachView() {
        if (isAttach()) {
            modelView!!.clear()
            modelView = null
        }
        unSubscribe()
    }

    private fun isSubscribe(): Boolean {
        if (disposableList != null && disposableList!!.size > 0) {
            return true
        } else {
            return false
        }
    }

    private fun unSubscribe() {
        if (isSubscribe()) {
            if (disposableList != null && disposableList!!.size > 0) {
                for (i in disposableList!!.indices) {
                    disposable = disposableList!!.get(i)
                    if (disposable != null && !disposable!!.isDisposed()) {
                        disposable!!.dispose()
                        disposable = null
                    }
                }
                disposableList!!.clear()
                disposableList = null
            }
        }
    }

}