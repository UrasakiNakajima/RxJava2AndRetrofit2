package com.phone.main_module.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.ActivityPageManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.ResourcesManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.common_library.manager.SystemManager;
import com.phone.main_module.R;
import com.phone.main_module.login.bean.GetVerificationCode;
import com.phone.main_module.login.bean.LoginResponse;
import com.phone.main_module.login.presenter.LoginPresenterImpl;
import com.phone.main_module.login.view.ILoginView;
import com.phone.main_module.main.MainActivity;

import java.util.Objects;

@Route(path = "/main_module/login")
public class LoginActivity extends BaseMvpRxAppActivity<IBaseView, LoginPresenterImpl>
        implements ILoginView {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private EditText edtUserId;
    private EditText edtPassword;
    private FrameLayout layoutVerificationCode;
    private EditText edtVerificationCode;
    private TextView tevGetAuthCode;
    private TextView tevLogin;
    private TextView tevJumpToRegister;

    private String userId;
    private String password;
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
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        edtUserId = (EditText) findViewById(R.id.edt_user_id);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        layoutVerificationCode = (FrameLayout) findViewById(R.id.layout_verification_code);
        edtVerificationCode = (EditText) findViewById(R.id.edt_verification_code);
        tevGetAuthCode = (TextView) findViewById(R.id.tev_get_auth_code);
        tevLogin = (TextView) findViewById(R.id.tev_login);
        tevJumpToRegister = (TextView) findViewById(R.id.tev_jump_to_register);

        setToolbar(true, R.color.color_FFFFFFFF);

        imvBack.setColorFilter(ResourcesManager.getColor(R.color.color_000000));
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
//                initLoginWithAuthCode();
                initLogin();
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
        edtUserId.setText("13510001000");
        edtPassword.setText("12345678");
    }

    @NonNull
    @Override
    protected LoginPresenterImpl attachPresenter() {
        return new LoginPresenterImpl(this);
    }

    @Override
    public void showLoading() {
        if (getLoadView() != null && !getLoadView().isShown()) {
            getLoadView().setVisibility(View.VISIBLE);
            getLoadView().start();
        }
    }

    @Override
    public void hideLoading() {
        if (getLoadView() != null && getLoadView().isShown()) {
            getLoadView().stop();
            getLoadView().setVisibility(View.GONE);
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

    @Override
    public void loginSuccess(String success) {
        Objects.requireNonNull(getBaseApplication()).setLogin(true);
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginError(String error) {
        showToast(error, true);
    }

    private void getAuthCode() {
        userId = edtUserId.getText().toString();
        //		verificationCode = edtVerificationCode.getText().toString();
        //		phoneDevice = UIUtils.getDeviceUUid().toString();

        getBodyParams().clear();
        getBodyParams().put("account", userId);
        Objects.requireNonNull(getPresenter()).getAuthCode(this, getBodyParams());
    }

    private void initLoginWithAuthCode() {
        userId = edtUserId.getText().toString();
        verificationCode = edtVerificationCode.getText().toString();
        phoneDevice = SystemManager.getSystemId();

        getBodyParams().clear();
        getBodyParams().put("account", userId);
        getBodyParams().put("captcha", verificationCode);
        getBodyParams().put("type", "1");//1 APP
        getBodyParams().put("phoneDevice", phoneDevice);
        Objects.requireNonNull(getPresenter()).loginWithAuthCode(this, getBodyParams());
    }

    private void initLogin() {
        if (RetrofitManager.isNetworkAvailable()) {
            userId = edtUserId.getText().toString();
            password = edtPassword.getText().toString();

            if (TextUtils.isEmpty(userId)) {
                showToast(ResourcesManager.getString(R.string.please_enter_one_user_name), true);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                showToast(ResourcesManager.getString(R.string.please_input_a_password), true);
                return;
            }
            getBodyParams().clear();
            getBodyParams().put("userId", userId);
            getBodyParams().put("password", password);
            Objects.requireNonNull(getPresenter()).login(this, getBodyParams());
        } else {
            showToast(ResourcesManager.getString(R.string.please_check_the_network_connection), true);
        }
    }

}
