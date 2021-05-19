package com.mobile.main_module.login.presenter;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface ILoginPresenter {
	
	void getAuthCode(AppCompatActivity activity, Map<String, String> bodyParams);
	
	void loginWithAuthCode(AppCompatActivity activity, Map<String, String> bodyParams);
	
	void register(Map<String, String> bodyParams);
	
}
