package com.phone.common_library.manager;

import android.Manifest;

import com.phone.common_library.callback.OnCommonRxPermissionsCallback;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragment;

import io.reactivex.functions.Consumer;

public class RxPermissionsManager {

    private static final String TAG = RxPermissionsManager.class.getSimpleName();
    private static RxPermissionsManager rxPermissionsManager;
    //    private RxPermissions rxPermissions;
    private RxPermissions rxPermissionsActivity;
    private RxPermissions rxPermissionsFragment;

    private RxPermissionsManager() {
    }

    public static RxPermissionsManager getInstance() {
        if (rxPermissionsManager == null) {
            synchronized (RxPermissionsManager.class) {
                if (rxPermissionsManager == null) {
                    rxPermissionsManager = new RxPermissionsManager();
                }
            }
        }

        return rxPermissionsManager;
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了
     */
    public void initRxPermissionsRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback) {
        if (rxPermissionsActivity == null) {
            rxPermissionsActivity = new RxPermissions(rxAppCompatActivity);
        }
        rxPermissionsActivity
                .requestEachCombined(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                //解决rxjava导致的内存泄漏问题
                .compose(rxAppCompatActivity.<Permission>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !

                            // 所有的权限都授予
                            LogManager.i(TAG, "所有的权限都授予");
                            LogManager.i(TAG, "用户已经同意该权限 permission.name*****" + permission.name);

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class);
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE);

                            onCommonRxPermissionsCallback.onRxPermissionsAllPass();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again

                            // 至少一个权限未授予且未勾选不再提示
                            LogManager.i(TAG, "至少一个权限未授予且未勾选不再提示");

                            onCommonRxPermissionsCallback.onNotCheckNoMorePromptError();
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings

                            // 至少一个权限未授予且勾选了不再提示
                            LogManager.i(TAG, "至少一个权限未授予且勾选了不再提示");

                            onCommonRxPermissionsCallback.onCheckNoMorePromptError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogManager.i(TAG, "throwable*****" + throwable.toString());
                        LogManager.i(TAG, "throwable message*****" + throwable.getMessage());

                        ExceptionManager.getInstance().throwException(rxAppCompatActivity, throwable);
                    }
                });
    }

    /**
     * RxFragment里需要的时候直接调用就行了
     */
    public void initRxPermissionsRxFragment(RxFragment rxFragment, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback) {
        if (rxPermissionsFragment == null) {
            rxPermissionsFragment = new RxPermissions(rxFragment);
        }
        rxPermissionsFragment
                .requestEachCombined(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
//                        , Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                //解决rxjava导致的内存泄漏问题
                .compose(rxFragment.<Permission>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !

                            // 用户已经同意该权限
                            LogManager.i(TAG, "用户已经同意该权限");

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class);
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE);

                            LogManager.i(TAG, "用户已经同意该权限 permission.name*****" + permission.name);

                            onCommonRxPermissionsCallback.onRxPermissionsAllPass();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again

                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogManager.i(TAG, "用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框");

                            onCommonRxPermissionsCallback.onNotCheckNoMorePromptError();
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings

                            // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                            LogManager.i(TAG, "用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限");

                            onCommonRxPermissionsCallback.onCheckNoMorePromptError();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogManager.i(TAG, "throwable*****" + throwable.toString());
                        LogManager.i(TAG, "throwable message*****" + throwable.getMessage());

                        ExceptionManager.getInstance().throwException(rxFragment.getContext(), throwable);
                    }
                });
    }

}
