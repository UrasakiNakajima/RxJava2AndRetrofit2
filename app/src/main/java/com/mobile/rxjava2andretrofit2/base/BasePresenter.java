package com.mobile.rxjava2andretrofit2.base;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */


public class BasePresenter<T> {

    private WeakReference<T> modelView;
    protected List<Disposable> disposableList;
    protected Disposable disposable;

    protected void attachView(T view) {
        modelView = new WeakReference<T>(view);
        disposableList = new ArrayList<>();
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
        unSubscribe();
    }

    private boolean isSubscribe() {
        if (disposableList != null && disposableList.size() > 0) {
            return true;
        }
        return false;
    }

    private void unSubscribe() {
        if (isSubscribe()) {
            for (int i = 0; i < disposableList.size(); i++) {
                disposable = disposableList.get(i);
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposable = null;
                }
            }
            disposableList.clear();
            disposableList = null;
        }
    }

}
