package com.phone.main_module.login.model;

import com.phone.common_library.manager.RetrofitManager;
import com.phone.main_module.login.request.LoginRequest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

public class LoginModelImpl implements ILoginModel {
	
	private static final String TAG = LoginModelImpl.class.getSimpleName();
	
	public LoginModelImpl() {
	}
	
	@Override
	public Observable<ResponseBody> getAuthCode(Map<String, String> bodyParams) {
		return RetrofitManager.getInstance().getRetrofit()
				   .create(LoginRequest.class)
				   .getAuthCode(bodyParams);
	}
	
	@Override
	public Observable<ResponseBody> loginWithAuthCode(Map<String, String> bodyParams) {
		return RetrofitManager.getInstance().getRetrofit()
				   .create(LoginRequest.class)
				   .getLoginWithAuthCodeData(bodyParams);
	}
	
	@Override
	public Observable<ResponseBody> register(Map<String, String> bodyParams) {
		return RetrofitManager.getInstance().getRetrofit()
				   .create(LoginRequest.class)
				   .getRegisterData(bodyParams);
	}
	
}
