package com.mobile.main_module.login.view;

import com.mobile.common_library.base.IBaseView;
import com.mobile.main_module.login.bean.GetVerificationCode;
import com.mobile.main_module.login.bean.LoginResponse;

public interface ILoginView extends IBaseView {
	
	void getAuthCodeSuccess(GetVerificationCode.DataDTO success);
	
	void getAuthCodeError(String error);
	
	void loginWithAuthCodeSuccess(LoginResponse.DataDTO success);
	
	void loginWithAuthCodeError(String error);
	
}
