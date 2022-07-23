package com.phone.main_module.login.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.phone.common_library.base.BaseMvpRxAppActivity;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.main_module.R;
import com.phone.main_module.login.presenter.LoginPresenterImpl;
import com.phone.main_module.login.view.IRegisterView;

public class RegisterActivity extends BaseMvpRxAppActivity<IBaseView, LoginPresenterImpl>
        implements IRegisterView {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Toolbar toolbar;
    private FrameLayout layoutBack;
    private ImageView imvBack;
    private EditText edtUserName;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private TextView tevRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        layoutBack = (FrameLayout) findViewById(R.id.layout_back);
        imvBack = (ImageView) findViewById(R.id.imv_back);
        edtUserName = (EditText) findViewById(R.id.edt_user_name);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm_password);
        tevRegister = (TextView) findViewById(R.id.tev_register);

        addContentView(loadView, layoutParams);
        setToolbar(true, R.color.color_FFFFFFFF);

        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tevRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRegister();
            }
        });
    }

    @Override
    protected void initLoadData() {
        edtUserName.setText("13510001000");
        edtPassword.setText("12345678");
        edtConfirmPassword.setText("12345678");
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
    public void registerSuccess(String success) {

    }

    @Override
    public void registerError(String error) {
        showToast(error, true);
    }

    private void initRegister() {
        if (RetrofitManager.isNetworkAvailable(this)) {
            bodyParams.clear();
            bodyParams.put("username", edtUserName.getText().toString());
            bodyParams.put("password", edtPassword.getText().toString());
            bodyParams.put("repassword", edtConfirmPassword.getText().toString());
//        String data = MapManager.mapToJsonStr(bodyParams);
//        String requestData = JSONObject.toJSONString(bodyParams);
//        LogManager.i(TAG, "requestData*****" + requestData);
            presenter.register(this, bodyParams);
        } else {
            showToast(getResources().getString(R.string.please_check_the_network_connection), true);
        }
    }

}
