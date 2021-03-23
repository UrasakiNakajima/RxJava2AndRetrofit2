package com.mobile.rxjava2andretrofit2.login.view;

import com.mobile.common_library.base.IBaseView;

public interface IRegisterView extends IBaseView {

    void registerSuccess(String success);

    void registerError(String error);

}
