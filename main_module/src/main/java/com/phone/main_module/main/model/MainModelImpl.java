package com.phone.main_module.main.model;

import com.phone.common_library.manager.RetrofitManager;
import com.phone.main_module.main.request.MainRequest;

import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class MainModelImpl implements IMainModel {

    private static final String TAG = "MainModelImpl";

    public MainModelImpl() {
    }

    @Override
    public Observable<ResponseBody> mainData(Map<String, String> bodyParams) {
        return Objects.requireNonNull(Objects.requireNonNull(RetrofitManager.Companion.get()).getRetrofit())
                .create(MainRequest.class)
                .getMainData(bodyParams);
    }

}
