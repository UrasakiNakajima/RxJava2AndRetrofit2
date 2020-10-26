package com.mobile.rxjava2andretrofit2.login.view;

import com.mobile.rxjava2andretrofit2.base.IBaseView;

public interface IAddShopView extends IBaseView {

    void addShopSuccess(String success);

    void addShopError(String error);
}
