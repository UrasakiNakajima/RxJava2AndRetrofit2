package com.mobile.rxjava2andretrofit2.login.view;

import com.mobile.common_library.base.IBaseView;
import com.mobile.rxjava2andretrofit2.login.bean.LoginResponse;

public interface ILoginView extends IBaseView {

    void loginSuccess(LoginResponse.DataBean success);

    void loginError(String error);

}
