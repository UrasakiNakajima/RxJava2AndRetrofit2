package com.phone.main_module.main.view;


import com.phone.common_library.base.IBaseView;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */


public interface IMainView extends IBaseView {

    void mainDataSuccess(String success);

    void mainDataError(String error);
}
