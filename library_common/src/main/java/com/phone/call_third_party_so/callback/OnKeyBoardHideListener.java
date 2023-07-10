package com.phone.call_third_party_so.callback;


import android.view.KeyEvent;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/4/7 10:01
 * introduce :
 */


public interface OnKeyBoardHideListener {

    void onKeyShow(int keyCode, KeyEvent event);

    void onKeyHide(int keyCode, KeyEvent event);
}
