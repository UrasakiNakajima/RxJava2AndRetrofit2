package com.mobile.rxjava2andretrofit2.login.presenter.base;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface ILoginPresenter {

    void login(String requestData);

    void addShop(Map<String, String> bodyParams, Map<String, File> fileMap, Map<String, List<File>> filesMap);
}
