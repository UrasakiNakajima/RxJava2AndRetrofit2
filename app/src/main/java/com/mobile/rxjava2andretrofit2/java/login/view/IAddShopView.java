package com.mobile.rxjava2andretrofit2.java.login.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;

public interface IAddShopView extends IBaseView {

    void addShopSuccess(String success);

    void addShopError(String error);
}
