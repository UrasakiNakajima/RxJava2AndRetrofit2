package com.phone.common_library.callback;

public interface OnCommonBothParamCallback<T> {

    void onSuccess(T success, String data);

    void onError(String error);
}
