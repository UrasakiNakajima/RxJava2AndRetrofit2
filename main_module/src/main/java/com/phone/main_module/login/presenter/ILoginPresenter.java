package com.phone.main_module.login.presenter;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.Map;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface ILoginPresenter {

    void getAuthCode(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);

    void loginWithAuthCode(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);

    void login(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);

    void register(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);

}
