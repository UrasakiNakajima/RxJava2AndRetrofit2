package com.phone.common_library.callback;


/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 15:12
 * introduce : 一个参数回调
 */


public interface OnCommonSingleParamCallback<T> {

    void onSuccess(T success);

    void onError(String error);
}
