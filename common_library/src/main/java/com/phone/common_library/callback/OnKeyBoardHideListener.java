package com.phone.common_library.callback;


import android.view.KeyEvent;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2020/4/7 10:01
 * introduce :
 */


public interface OnKeyBoardHideListener {

    void onKeyShow(int keyCode, KeyEvent event);

    void onKeyHide(int keyCode, KeyEvent event);
}
