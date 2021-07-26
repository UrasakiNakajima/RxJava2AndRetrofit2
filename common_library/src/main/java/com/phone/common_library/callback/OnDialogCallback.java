package com.phone.common_library.callback;

import android.view.View;

import java.util.Map;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/5/17 15:49
 * introduce :
 */

public interface OnDialogCallback<T> {
	
	void onDialogClick(View view, T success);
	
	void onDialogClick(View view, T success, Map<String, String> params);
}
