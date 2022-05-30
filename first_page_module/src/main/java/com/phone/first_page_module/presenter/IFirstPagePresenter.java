package com.phone.first_page_module.presenter;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IFirstPagePresenter {
	
	void firstPage(Fragment fragment, Map<String, String> bodyParams);
	
	void firstPage(AppCompatActivity appCompatActivity, Map<String, String> bodyParams);
	
}
