package com.mobile.rxjava2andretrofit2.login.model;

import com.alibaba.fastjson.JSONObject;
import com.mobile.rxjava2andretrofit2.login.model.base.ILoginModel;
import com.mobile.rxjava2andretrofit2.login.request.LoginRequest;
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class LoginModelImpl implements ILoginModel {

    private static final String TAG = "LoginModelImpl";

    public LoginModelImpl() {
    }

    @Override
    public Observable<ResponseBody> login(RequestBody requestBody) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(LoginRequest.class)
                .getLoginData(requestBody);
    }

    @Override
    public Observable<ResponseBody> addShop(RequestBody requestBody) {
        return RetrofitManager.getInstance().getRetrofit()
                .create(LoginRequest.class)
                .addShopResult(requestBody);
    }

}
