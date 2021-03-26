package com.mobile.main_module.login.view;

import com.mobile.common_library.base.IBaseView;
import com.mobile.main_module.login.bean.LoginResponse;

public interface ILoginView extends IBaseView {

    void loginSuccess(LoginResponse.DataBean success);

    void loginError(String error);

}
