package com.mobile.rxjava2andretrofit2.login.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BaseMvpAppActivity;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.login.bean.LoginResponse;
import com.mobile.rxjava2andretrofit2.login.presenter.LoginPresenterImpl;
import com.mobile.rxjava2andretrofit2.login.view.ILoginView;
import com.mobile.rxjava2andretrofit2.manager.LogManager;

import java.util.ArrayList;
import java.util.List;

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

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,};
    private static final int REQUEST_CODE = 0xa1;//请求多个权限
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    private List<String> mPermissionList = new ArrayList<>();
    private AlertDialog mPermissionsDialog;
    private Intent intent;

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
        initPermissions();
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

    private void initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionList.clear();//清空没有通过的权限
            //逐个判断你要的权限是否已经通过
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);//添加还未授予的权限
                }
            }

            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 207) {
            initPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissionDismiss = false;//有权限没有通过
        if (REQUEST_CODE == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    hasPermissionDismiss = true;
                }
            }

            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                //跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        LogManager.i(TAG, "onRequestPermissionsResult shouldShowRequestPermissionRationale*****" + ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]));
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            showSystemSetupDialog();
                        } else {
                            showPermissionsDialog();
                        }
                    }
                }
            }
        }
    }

    private void showSystemSetupDialog() {
        cancelPermissionsDialog();
        if (mPermissionsDialog == null) {
            mPermissionsDialog = new AlertDialog.Builder(this)
                    .setTitle("权限设置")
                    .setMessage("获取相关权限失败，将导致部分功能无法正常使用，请到设置页面手动授权")
                    .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionsDialog();
                            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 207);
                        }
                    })
                    .create();
        }

        mPermissionsDialog.setCancelable(false);
        mPermissionsDialog.show();
    }

    private void showPermissionsDialog() {
        cancelPermissionsDialog();
        String title;
        String message;
        if (mPermissionsDialog == null) {
            title = "请授予权限";
            message = "已禁用权限，请手动授予";
            mPermissionsDialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionsDialog();

                            initPermissions();
                        }
                    })
                    .create();
        }

        mPermissionsDialog.setCancelable(false);
        mPermissionsDialog.show();
    }

    //关闭对话框
    private void cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog.cancel();
            mPermissionsDialog = null;
        }
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
