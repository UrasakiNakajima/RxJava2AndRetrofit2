package com.mobile.rxjava2andretrofit2.java.login.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;

public interface IRegisterView extends IBaseView {

    void registerSuccess(String success);

    void registerError(String error);

}
