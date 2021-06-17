package com.mobile.first_page_module.presenter;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IFirstPagePresenter {
	
	void firstPage(Fragment fragment, Map<String, String> bodyParams);
	
	void firstPage(AppCompatActivity appCompatActivity, Map<String, String> bodyParams);
	
	void firstPageDetails(Map<String, String> bodyParams);
}
