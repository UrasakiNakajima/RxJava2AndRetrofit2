package com.phone.main_module.login.view;

import com.phone.common_library.base.IBaseView;
import com.phone.main_module.login.bean.GetVerificationCode;
import com.phone.main_module.login.bean.LoginResponse;

public interface ILoginView extends IBaseView {
	
	void getAuthCodeSuccess(GetVerificationCode.DataDTO success);
	
	void getAuthCodeError(String error);
	
	void loginWithAuthCodeSuccess(LoginResponse.DataDTO success);
	
	void loginWithAuthCodeError(String error);

	void loginSuccess(String success);

	void loginError(String error);

}
