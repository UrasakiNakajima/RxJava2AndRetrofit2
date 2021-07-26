package com.phone.main_module.login.view;

import com.phone.common_library.base.IBaseView;

public interface IRegisterView extends IBaseView {

    void registerSuccess(String success);

    void registerError(String error);

}
