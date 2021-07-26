package com.phone.common_library.base;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.Disposable;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */


public class BasePresenter<T> {

    private   WeakReference<T> modelView;
//    protected CompositeDisposable compositeDisposable;
    protected Disposable       disposable;

    protected void attachView(T view) {
        modelView = new WeakReference<T>(view);
//        compositeDisposable = new CompositeDisposable();
    }

    protected T obtainView() {
        return isAttach() ? modelView.get() : null;
    }

    private boolean isAttach() {
        return modelView != null &&
                modelView.get() != null;
    }

    protected void detachView() {
        if (isAttach()) {
            modelView.clear();
            modelView = null;
        }
//        unSubscribe();
    }

//    protected void unSubscribe() {
//        if (compositeDisposable != null && compositeDisposable.size() > 0) {
//            compositeDisposable.clear();
//            compositeDisposable = null;
//        }
//    }


}
