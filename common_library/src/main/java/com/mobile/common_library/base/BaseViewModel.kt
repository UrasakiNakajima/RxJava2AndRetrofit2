package com.mobile.common_library.base

import androidx.lifecycle.ViewModel
import com.mobile.common_library.manager.LogManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val TAG: String = "BaseViewModel"
    protected var compositeDisposable: CompositeDisposable = CompositeDisposable()
    protected var disposable: Disposable? = null

    protected fun unSubscribe() {
        if (compositeDisposable != null && compositeDisposable.size() > 0) {
            compositeDisposable.clear()
        }
    }

    override fun onCleared() {
        LogManager.i(TAG, "onCleared")
        unSubscribe();
        super.onCleared()
    }
}