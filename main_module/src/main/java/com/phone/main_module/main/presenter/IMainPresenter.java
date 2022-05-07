package com.phone.main_module.main.presenter;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.Map;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IMainPresenter {

    void mainData(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);
}
