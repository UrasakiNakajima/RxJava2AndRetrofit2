package com.phone.library_common.callback;

public interface OnCommonBothParamCallback<T> {

    void onSuccess(T success, String data);

    void onError(String error);
}
