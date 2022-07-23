package com.phone.main_module.login.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.phone.common_library.base.BaseRxAppActivity;
import com.phone.common_library.manager.LogManager;
import com.phone.main_module.R;
import com.phone.main_module.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends BaseRxAppActivity {

    private static final String TAG = LaunchActivity.class.getSimpleName();

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
        return R.layout.activity_launch;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initLoadData() {
//        initPermissions();

        if (baseApplication.isLogin()) {
            startActivity(MainActivity.class);
        } else {
            startActivity(MainActivity.class);
        }
        finish();
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
            } else {
                if (baseApplication.isLogin()) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(MainActivity.class);
                }
                finish();
            }
        } else {
            if (baseApplication.isLogin()) {
                startActivity(MainActivity.class);
            } else {
                startActivity(MainActivity.class);
            }
            finish();
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
                            break;
                        } else {
                            showPermissionsDialog();
                            break;
                        }
                    }
                }
            } else {
                if (baseApplication.isLogin()) {
                    startActivity(MainActivity.class);
                } else {
                    startActivity(MainActivity.class);
                }
                finish();
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

    /**
     * 展示权限对话框
     */
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

    /**
     * 关闭对话框
     */
    private void cancelPermissionsDialog() {
        if (mPermissionsDialog != null) {
            mPermissionsDialog.cancel();
            mPermissionsDialog = null;
        }
    }

}
