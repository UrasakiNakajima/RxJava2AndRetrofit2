package com.mobile.main_module.login.model;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

public interface ILoginModel {

    Observable<ResponseBody> login(Map<String, String> bodyParams);

    Observable<ResponseBody> register(Map<String, String> bodyParams);

}
