package com.phone.library_common.manager

import com.phone.library_common.callback.OnCommonRxPermissionsCallback
import com.phone.library_common.manager.LogManager.i
import com.tbruyelle.rxpermissions2.RxPermissions
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

class RxPermissionsManager {

    /**
     * 保证只有一个实例
     *
     * @return
     */
    companion object {
        private val TAG = RxPermissionsManager::class.java.simpleName
        private var instance: RxPermissionsManager? = null
            get() {
                if (field == null) {
                    field = RxPermissionsManager()
                }
                return field
            }

        //Synchronized添加后就是线程安全的的懒汉模式
        @Synchronized
        @JvmStatic
        fun instance(): RxPermissionsManager {
            return instance!!
        }
    }

    /**
     * RxAppCompatActivity里需要的时候直接调用就行了（不會拋出異常）
     */
    fun initRxPermissions(
        rxAppCompatActivity: RxAppCompatActivity,
        permissions: Array<String>,
        onCommonRxPermissionsCallback: OnCommonRxPermissionsCallback
    ) {
        //    private RxPermissions rxPermissions
        val rxActivityPermissions = RxPermissions(rxAppCompatActivity)
        rxActivityPermissions
            .requestEachCombined(*permissions) //解决rxjava导致的内存泄漏问题
            .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe { permission ->
                if (permission.granted) {
                    // `permission.name` is granted !

                    // 所有的权限都授予
                    i(TAG, "所有的权限都授予")
                    i(
                        TAG,
                        "所有的权限都授予 permission.name*****" + permission.name
                    )

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class)
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE)
                    onCommonRxPermissionsCallback.onRxPermissionsAllPass()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again

                    // 至少一个权限未授予且未勾选不再提示
                    i(TAG, "至少一个权限未授予且未勾选不再提示")
                    onCommonRxPermissionsCallback.onNotCheckNoMorePromptError()
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings

                    // 至少一个权限未授予且勾选了不再提示
                    i(TAG, "至少一个权限未授予且勾选了不再提示")
                    onCommonRxPermissionsCallback.onCheckNoMorePromptError()
                }
            }
    }

    /**
     * RxFragment里需要的时候直接调用就行了（不會拋出異常）
     */
    fun initRxPermissions2(
        rxFragment: RxFragment,
        permissions: Array<String>,
        onCommonRxPermissionsCallback: OnCommonRxPermissionsCallback
    ) {
        val rxFragmentPermissions = RxPermissions(rxFragment)
        rxFragmentPermissions
            .requestEachCombined(*permissions) //解决rxjava导致的内存泄漏问题
            .compose(rxFragment.bindUntilEvent(FragmentEvent.DESTROY))
            .subscribe { permission ->
                if (permission.granted) {
                    // `permission.name` is granted !

                    // 所有的权限都授予
                    i(TAG, "所有的权限都授予")
                    i(
                        TAG,
                        "所有的权限都授予 permission.name*****" + permission.name
                    )

//                        Intent bindIntent = new Intent(this, Base64AndFileService.class)
//                        // 绑定服务和活动，之后活动就可以去调服务的方法了
//                        bindService(bindIntent, connection, BIND_AUTO_CREATE)
                    onCommonRxPermissionsCallback.onRxPermissionsAllPass()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again

                    // 至少一个权限未授予且未勾选不再提示
                    i(TAG, "至少一个权限未授予且未勾选不再提示")
                    onCommonRxPermissionsCallback.onNotCheckNoMorePromptError()
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings

                    // 至少一个权限未授予且勾选了不再提示
                    i(TAG, "至少一个权限未授予且勾选了不再提示")
                    onCommonRxPermissionsCallback.onCheckNoMorePromptError()
                }
            }
    }

}