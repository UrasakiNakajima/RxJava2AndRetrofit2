package com.mobile.main_module.login.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.mobile.common_library.base.BaseMvpAppActivity;
import com.mobile.common_library.base.IBaseView;
import com.mobile.main_module.R;
import com.mobile.main_module.R2;
import com.mobile.main_module.login.bean.LoginResponse;
import com.mobile.main_module.login.presenter.LoginPresenterImpl;
import com.mobile.main_module.login.view.ILoginView;
import com.mobile.main_module.main.MainActivity;

import butterknife.BindView;

public class LoginActivity extends BaseMvpAppActivity<IBaseView, LoginPresenterImpl>
        implements ILoginView {

    private static final String TAG = "LoginActivity";
    @BindView(R2.id.imv_back)
    ImageView imvBack;
    @BindView(R2.id.layout_back)
    FrameLayout layoutBack;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.edt_user_name)
    EditText edtUserName;
    @BindView(R2.id.edt_password)
    EditText edtPassword;
    @BindView(R2.id.tev_login)
    TextView tevLogin;
    @BindView(R2.id.tev_jump_to_register)
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

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tevLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        startActivity(MainActivity.class);
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

}
