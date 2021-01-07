package com.mobile.rxjava2andretrofit2.base

import androidx.lifecycle.ViewModel
import com.mobile.rxjava2andretrofit2.manager.LogManager
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val TAG: String = "BaseViewModel"
    protected var disposableList: MutableList<Disposable> = mutableListOf()
    protected var disposable: Disposable? = null

    private fun isSubscribe(): Boolean {
        if (disposableList != null && disposableList.size > 0) {
            return true
        } else {
            return false
        }
    }

    protected fun unSubscribe() {
        if (isSubscribe()) {
            if (disposableList != null && disposableList.size > 0) {
                for (i in disposableList.indices) {
                    disposable = disposableList.get(i)
                    if (disposable != null && !disposable!!.isDisposed()) {
                        disposable!!.dispose()
                        disposable = null
                    }
                }
                disposableList.clear()
            }
        }
    }

    override fun onCleared() {
        LogManager.i(TAG, "onCleared")
        unSubscribe();
        super.onCleared()
    }
}