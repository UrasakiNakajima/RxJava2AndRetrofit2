package com.mobile.common_library.base;


import java.lang.ref.WeakReference;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/7 11:19
 * introduce :
 */


public class BaseModel<T> {

    private WeakReference<T> modelContext;

    protected void attachContext(T modelContext) {
        this.modelContext = new WeakReference<T>(modelContext);
    }

    protected T obtainContext() {
        return isAttach() ? modelContext.get() : null;
    }

    private boolean isAttach() {
        return modelContext != null &&
                modelContext.get() != null;
    }

    protected boolean isEmpty(String dataStr) {
        if (dataStr != null && !"".equals(dataStr)) {
            return false;
        } else {
            return true;
        }
    }

    public void detachContext() {
        if (isAttach()) {
            modelContext.clear();
            modelContext = null;
        }
    }

}
