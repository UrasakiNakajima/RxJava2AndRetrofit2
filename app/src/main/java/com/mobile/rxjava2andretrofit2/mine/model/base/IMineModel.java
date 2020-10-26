package com.mobile.rxjava2andretrofit2.mine.model.base;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

public interface IMineModel {

    Observable<ResponseBody> mineData(Map<String, String> bodyParams);

    Observable<ResponseBody> submitFeedback(Map<String, String> bodyParams);

}
