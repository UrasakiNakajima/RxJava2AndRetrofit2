package com.mobile.rxjava2andretrofit2.mine.presenter.base;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IMinePresenter {

    void mineData(Map<String, String> bodyParams);

    void submitFeedback(Map<String, String> bodyParams);

}
