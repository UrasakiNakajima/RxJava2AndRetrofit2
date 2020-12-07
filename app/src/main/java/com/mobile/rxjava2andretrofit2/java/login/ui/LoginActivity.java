package com.mobile.rxjava2andretrofit2.java.login.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.java.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.login.bean.LoginResponse;
import com.mobile.rxjava2andretrofit2.java.login.presenter.LoginPresenterImpl;
import com.mobile.rxjava2andretrofit2.java.login.view.ILoginView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpAppActivity<IBaseView, LoginPresenterImpl>
        implements ILoginView {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.imv_back)
    ImageView imvBack;
    @BindView(R.id.layout_back)
    FrameLayout layoutBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.tev_login)
    TextView tevLogin;
    @BindView(R.id.tev_jump_to_register)
    TextView tevJumpToRegister;

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
        edtUserName.setText("13510001000");
        edtPassword.setText("12345678");
    }

    @Override
    protected void initViews() {
        addContentView(loadView, layoutParams);
        setToolbar(true, R.color.color_FFFFFFFF);
    }

    @Override
    protected void initLoadData() {

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
    public void loginSuccess(LoginResponse.DataBean success) {
        showToast(success.getUserName(), true);
        startActivity(AddShopActivity.class);
    }

    @Override
    public void loginError(String error) {
        showToast(error, true);
    }

    private void initLogin() {
        bodyParams.clear();
        bodyParams.put("username", edtUserName.getText().toString());
        bodyParams.put("password", edtPassword.getText().toString());
//        String data = MapManager.mapToJsonStr(bodyParams);
//        String requestData = JSONObject.toJSONString(bodyParams);
//        LogManager.i(TAG, "requestData*****" + requestData);
        presenter.login(bodyParams);
    }

    @OnClick({R.id.layout_back, R.id.tev_login, R.id.tev_jump_to_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                break;
            case R.id.tev_login:
                initLogin();
                break;
            case R.id.tev_jump_to_register:
                startActivity(RegisterActivity.class);
                break;
        }
    }
}
