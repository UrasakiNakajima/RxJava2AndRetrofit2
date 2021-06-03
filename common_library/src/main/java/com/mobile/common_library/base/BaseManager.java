package com.mobile.common_library.base;


import java.lang.ref.WeakReference;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 10:55
 * introduce :
 */


public abstract class BaseManager<T> {

    private WeakReference<T> modelActivity;

    protected void attachActivity(T modelActivity) {
        this.modelActivity = new WeakReference<T>(modelActivity);
    }

    public void detachActivity() {
        if (isAttach()) {
            modelActivity.clear();
            modelActivity = null;
        }
    }

    public T obtainActivity() {
        return isAttach() ? modelActivity.get() : null;
    }

    protected boolean isAttach() {
        return modelActivity != null &&
                modelActivity.get() != null;
    }

    protected boolean isEmpty(String dataStr) {
        if (dataStr != null && !"".equals(dataStr)) {
            return false;
        } else {
            return true;
        }
    }

}
