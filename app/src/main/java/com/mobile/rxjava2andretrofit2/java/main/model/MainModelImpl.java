package com.mobile.rxjava2andretrofit2.java.main.model;

import com.mobile.rxjava2andretrofit2.java.main.model.base.IMainModel;
import com.mobile.rxjava2andretrofit2.java.main.request.MainRequest;
import com.mobile.rxjava2andretrofit2.java.manager.RetrofitManager;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class MainModelImpl implements IMainModel {

    private static final String TAG = "FirstPageModelImpl";

    public MainModelImpl() {
    }

    @Override
    public Observable<ResponseBody> mainData(Map<String, String> bodyParams) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(MainRequest.class)
                .getMainData(bodyParams);
    }

}
