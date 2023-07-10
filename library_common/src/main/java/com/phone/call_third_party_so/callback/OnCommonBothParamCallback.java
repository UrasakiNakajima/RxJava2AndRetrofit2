package com.phone.call_third_party_so.callback;

public interface OnCommonBothParamCallback<T> {

    void onSuccess(T success, String data);

    void onError(String error);
}
