package com.phone.common_library.base;

import java.lang.ref.WeakReference;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 10:55
 * introduce :
 */


public class BasePresenter<T> {

    private WeakReference<T> modelView;

    protected void attachView(T view) {
        modelView = new WeakReference<>(view);
    }

    protected T obtainView() {
        return isAttach() ? modelView.get() : null;
    }

    private boolean isAttach() {
        return modelView != null &&
                modelView.get() != null;
    }

    public void detachView() {
        if (isAttach()) {
            modelView.clear();
            modelView = null;
        }
    }

}
