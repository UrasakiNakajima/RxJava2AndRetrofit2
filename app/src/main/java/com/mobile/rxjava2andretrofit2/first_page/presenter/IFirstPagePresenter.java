package com.mobile.rxjava2andretrofit2.first_page.presenter;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IFirstPagePresenter {

    void firstPage(Map<String, String> bodyParams);

    void firstPageDetails(Map<String, String> bodyParams);
}
