package com.mobile.main_module.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.mobile.common_library.base.BaseMvpAppActivity;
import com.mobile.common_library.base.IBaseView;
import com.mobile.common_library.manager.ActivityPageManager;
import com.mobile.common_library.manager.DeviceManager;
import com.mobile.common_library.manager.LogManager;
import com.mobile.main_module.R;
import com.mobile.main_module.login.bean.GetVerificationCode;
import com.mobile.main_module.login.bean.LoginResponse;
import com.mobile.main_module.login.presenter.LoginPresenterImpl;
import com.mobile.main_module.login.view.ILoginView;
import com.mobile.main_module.main.MainActivity;

import androidx.appcompat.widget.Toolbar;

@Route(path = "/main_module/login")
public class LoginActivity extends BaseMvpAppActivity<IBaseView, LoginPresenterImpl>
	implements ILoginView {
	
	private static final String      TAG = "LoginActivity";
	private              Toolbar     toolbar;
	private              FrameLayout layoutBack;
	private              ImageView   imvBack;
	private              EditText    edtAccountNumber;
	private              EditText    edtPassword;
	private              FrameLayout layoutVerificationCode;
	private              EditText    edtVerificationCode;
	private              TextView    tevGetAuthCode;
	private              TextView    tevLogin;
	private              TextView    tevJumpToRegister;
	
	private String accountNumber;
	private String verificationCode;
	private String phoneDevice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected int initLayoutId() {
		return R.layout.activity_login;
	}
	
	@Override
	protected void initData() {
		ActivityPageManager.getInstance().finishAllActivityExcept(LoginActivity.class);
	}
	
	@Override
	protected void initViews() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		layoutBack = (FrameLayout) findViewById(R.id.layout_back);
		imvBack = (ImageView) findViewById(R.id.imv_back);
		edtAccountNumber = (EditText) findViewById(R.id.edt_account_number);
		edtPassword = (EditText) findViewById(R.id.edt_password);
		layoutVerificationCode = (FrameLayout) findViewById(R.id.layout_verification_code);
		edtVerificationCode = (EditText) findViewById(R.id.edt_verification_code);
		tevGetAuthCode = (TextView) findViewById(R.id.tev_get_auth_code);
		tevLogin = (TextView) findViewById(R.id.tev_login);
		tevJumpToRegister = (TextView) findViewById(R.id.tev_jump_to_register);
		
		addContentView(loadView, layoutParams);
		setToolbar(true, R.color.color_FFFFFFFF);
		
		layoutBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tevGetAuthCode.setOnClickListener(view -> {
			getAuthCode();
		});
		tevLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initLoginWithAuthCode();
			}
		});
		tevJumpToRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(RegisterActivity.class);
			}
		});
	}
	
	@Override
	protected void initLoadData() {
		edtAccountNumber.setText("13513313214");
	}
	
	@Override
	protected LoginPresenterImpl attachPresenter() {
		return new LoginPresenterImpl(this);
	}
	
	@Override
	public void showLoading() {
		if (loadView != null && !loadView.isShown()) {
			loadView.setVisibility(View.VISIBLE);
			loadView.start();
		}
	}
	
	@Override
	public void hideLoading() {
		if (loadView != null && loadView.isShown()) {
			loadView.stop();
			loadView.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogManager.i(TAG, "onNewIntent");
	}
	
	@Override
	public void getAuthCodeSuccess(GetVerificationCode.DataDTO success) {
		showToast(success.getContent(), true);
	}
	
	@Override
	public void getAuthCodeError(String error) {
		showToast(error, true);
	}
	
	@Override
	public void loginWithAuthCodeSuccess(LoginResponse.DataDTO success) {
		//		showToast(success.getUserName(), true);
		startActivity(MainActivity.class);
		finish();
	}
	
	@Override
	public void loginWithAuthCodeError(String error) {
		showToast(error, true);
	}
	
	private void getAuthCode() {
		accountNumber = edtAccountNumber.getText().toString();
		//		verificationCode = edtVerificationCode.getText().toString();
		//		phoneDevice = UIUtils.getDeviceUUid().toString();
		
		bodyParams.clear();
		bodyParams.put("account", accountNumber);
		presenter.getAuthCode(this, bodyParams);
	}
	
	private void initLoginWithAuthCode() {
		accountNumber = edtAccountNumber.getText().toString();
		verificationCode = edtVerificationCode.getText().toString();
		phoneDevice = DeviceManager.getDeviceUUid();
		
		bodyParams.clear();
		bodyParams.put("account", accountNumber);
		bodyParams.put("captcha", verificationCode);
		bodyParams.put("type", "1");//1 APP
		bodyParams.put("phoneDevice", phoneDevice);
		presenter.loginWithAuthCode(this, bodyParams);
	}
	
}
