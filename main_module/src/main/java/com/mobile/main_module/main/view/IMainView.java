package com.mobile.main_module.main.view;


import com.mobile.common_library.base.IBaseView;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 15:44
 * introduce :
 */


public interface IMainView extends IBaseView {

    void mainDataSuccess(String success);

    void mainDataError(String error);
}
