package com.mobile.rxjava2andretrofit2.java.login.view;

import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.login.bean.LoginResponse;

public interface ILoginView extends IBaseView {

    void loginSuccess(LoginResponse.DataBean success);

    void loginError(String error);

}
