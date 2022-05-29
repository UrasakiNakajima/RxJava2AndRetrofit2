package com.phone.common_library.callback;

public interface OnCommonRxPermissionsCallback {

    /**
     * 所有的权限都授予
     */
    void onRxPermissionsAllPass();

    /**
     * 至少一个权限未授予且未勾选不再提示
     */
    void onNotCheckNoMorePromptError();

    /**
     * 至少一个权限未授予且勾选了不再提示
     */
    void onCheckNoMorePromptError();

}
