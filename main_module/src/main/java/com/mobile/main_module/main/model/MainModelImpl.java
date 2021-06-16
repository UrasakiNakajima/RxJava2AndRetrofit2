package com.mobile.main_module.main.model;

import com.mobile.common_library.manager.RetrofitManager;
import com.mobile.main_module.main.request.MainRequest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class MainModelImpl implements IMainModel {

    private static final String TAG = "MainModelImpl";

    public MainModelImpl() {
    }

    @Override
    public Observable<ResponseBody> mainData(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(MainRequest.class)
                .getMainData(bodyParams);
    }

}
