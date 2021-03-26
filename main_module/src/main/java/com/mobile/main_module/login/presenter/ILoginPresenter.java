package com.mobile.main_module.login.presenter;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface ILoginPresenter {

    void login(Map<String, String> bodyParams);

    void register(Map<String, String> bodyParams);

}
