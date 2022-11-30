package com.phone.module_main.main.model;

import com.phone.library_common.manager.RetrofitManager;
import com.phone.module_main.main.request.MainRequest;

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
