package com.phone.common_library.manager;

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
    //    private static RxPermissionsManager rxPermissionsManager;
    //    private RxPermissions rxPermissions;
    private RxPermissions rxActivityPermissions;
    private RxPermissions rxFragmentPermissions;


//    private RxPermissionsManager() {
//    }
//
//    public static RxPermissionsManager getInstance() {
//        if (rxPermissionsManager == null) {
//            synchronized (RxPermissionsManager.class) {
//                if (rxPermissionsManager == null) {
//                    rxPermissionsManager = new RxPermissionsManager();
//                }
//            }
//        }
//
//        return rxPermissionsManager;
//    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了（會拋出異常）
     */
    public void initRxPermissionsRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback, String... permissions) {
        rxActivityPermissions = new RxPermissions(rxAppCompatActivity);
        rxActivityPermissions
                .requestEachCombined(permissions)
                //解决rxjava导致的内存泄漏问题
                .compose(rxAppCompatActivity.<Permission>bindToLifecycle())
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
     * RxAppCompatActivity里需要的时候直接调用就行了（不會拋出異常）
     */
    public void initRxPermissionsRxAppCompatActivity2(RxAppCompatActivity rxAppCompatActivity, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback, String... permissions) {
        rxActivityPermissions = new RxPermissions(rxAppCompatActivity);
        rxActivityPermissions
                .requestEachCombined(permissions)
                //解决rxjava导致的内存泄漏问题
                .compose(rxAppCompatActivity.<Permission>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !

                            // 所有的权限都授予
                            LogManager.i(TAG, "所有的权限都授予");
                            LogManager.i(TAG, "所有的权限都授予 permission.name*****" + permission.name);

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
                });
    }

    /**
     * RxFragment里需要的时候直接调用就行了（會拋出異常）
     */
    public void initRxPermissionsRxFragment(RxFragment rxFragment, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback, String... permissions) {
        rxFragmentPermissions = new RxPermissions(rxFragment);
        rxFragmentPermissions
                .requestEachCombined(permissions)
                //解决rxjava导致的内存泄漏问题
                .compose(rxFragment.<Permission>bindToLifecycle())
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !

                            // 所有的权限都授予
                            LogManager.i(TAG, "所有的权限都授予");
                            LogManager.i(TAG, "所有的权限都授予 permission.name*****" + permission.name);


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

                        ExceptionManager.getInstance().throwException(rxFragment.getContext(), throwable);
                    }
                });
    }

    /**
     * RxFragment里需要的时候直接调用就行了（不會拋出異常）
     */
    public void initRxPermissionsRxFragment2(RxFragment rxFragment, OnCommonRxPermissionsCallback onCommonRxPermissionsCallback, String... permissions) {
        rxFragmentPermissions = new RxPermissions(rxFragment);
        rxFragmentPermissions
                .requestEachCombined(permissions)
                //解决rxjava导致的内存泄漏问题
                .compose(rxFragment.<Permission>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !

                            // 所有的权限都授予
                            LogManager.i(TAG, "所有的权限都授予");
                            LogManager.i(TAG, "所有的权限都授予 permission.name*****" + permission.name);

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
                });
    }

}
