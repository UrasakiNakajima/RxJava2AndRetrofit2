package com.phone.first_page_module.presenter;

import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragment;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:58
 * introduce :
 */

public interface IFirstPagePresenter {
	
	void firstPageRxFragment(RxFragment rxFragment, Map<String, String> bodyParams);
	
	void firstPageRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams);
	
}
