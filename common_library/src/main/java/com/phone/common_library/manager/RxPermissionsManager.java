package com.phone.common_library.manager;

import android.Manifest;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class RxPermissionsManager {

    private static final String TAG = RxPermissionsManager.class.getSimpleName();
    private FragmentActivity activity;
    private Fragment fragment;
    private RxPermissions rxPermissions;
    private static RxPermissionsManager rxPermissionsManager;

    public RxPermissionsManager(FragmentActivity activity) {
        this.activity = activity;
    }

    public static RxPermissionsManager getInstance(FragmentActivity activity) {
        if (rxPermissionsManager == null) {
            synchronized (RxPermissionsManager.class) {
                if (rxPermissionsManager == null) {
                    rxPermissionsManager = new RxPermissionsManager(activity);
                }
            }
        }

        return rxPermissionsManager;
    }

    public RxPermissionsManager(Fragment fragment) {
        this.fragment = fragment;
    }

    public static RxPermissionsManager getInstance(Fragment fragment) {
        if (rxPermissionsManager == null) {
            synchronized (RxPermissionsManager.class) {
                if (rxPermissionsManager == null) {
                    rxPermissionsManager = new RxPermissionsManager(fragment);
                }
            }
        }

        return rxPermissionsManager;
    }

    public void initRxPermissionsActivity(OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        rxPermissions = new RxPermissions(activity);
        rxPermissions
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                        // 用户已经同意该权限
                        LogManager.i(TAG, "用户已经同意该权限");

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class);
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE);

                        LogManager.i(TAG, "用户已经同意该权限 permission.name*****" + permission.name);

                        onCommonSingleParamCallback.onSuccess("");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogManager.i(TAG, "用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");

                        onCommonSingleParamCallback.onSuccess("");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                        LogManager.i(TAG, "用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限");


                        onCommonSingleParamCallback.onSuccess("");
                    }
                });
    }

    private void initRxPermissionsFragment(OnCommonSingleParamCallback<String> onCommonSingleParamCallback) {
        rxPermissions = new RxPermissions(fragment);
        rxPermissions
                .requestEach(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                .subscribe(permission -> { // will emit 2 Permission objects
                    if (permission.granted) {
                        // `permission.name` is granted !

                        // 用户已经同意该权限
                        LogManager.i(TAG, "用户已经同意该权限");

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class);
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE);

                        LogManager.i(TAG, "用户已经同意该权限 permission.name*****" + permission.name);

                        onCommonSingleParamCallback.onSuccess("");
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again

                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        LogManager.i(TAG, "用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");

                        onCommonSingleParamCallback.onSuccess("");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings

                        // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                        LogManager.i(TAG, "用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限");


                        onCommonSingleParamCallback.onSuccess("");
                    }
                });
    }

}
