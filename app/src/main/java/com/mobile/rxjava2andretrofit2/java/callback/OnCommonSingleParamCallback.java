package com.mobile.rxjava2andretrofit2.java.callback;


/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 15:12
 * introduce : 一个参数回调
 */


public interface OnCommonSingleParamCallback<T> {

    void onSuccess(T success);

    void onError(String error);
}
