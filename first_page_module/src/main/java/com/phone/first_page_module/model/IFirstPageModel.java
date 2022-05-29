package com.phone.first_page_module.model;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 16:57
 * introduce :
 */

public interface IFirstPageModel {
	
	Observable<ResponseBody> firstPage(Map<String, String> bodyParams);
	
	Observable<ResponseBody> firstPageDetails(Map<String, String> bodyParams);
	
}
