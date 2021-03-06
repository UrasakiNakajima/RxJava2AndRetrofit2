package com.phone.common_library.interceptor;

import android.text.TextUtils;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.common.ConstantData;
import com.phone.common_library.manager.LogManager;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlManagerInterceptor implements Interceptor {
	
	private static final String          TAG = BaseUrlManagerInterceptor.class.getSimpleName();
	private              BaseApplication baseApplication;
	
	public BaseUrlManagerInterceptor(BaseApplication baseApplication) {
		super();
		this.baseApplication = baseApplication;
	}
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		//获取原始的originalRequest
		Request originalRequest = chain.request();
		//获取originalRequest的创建者builder
		Request.Builder builder = originalRequest.newBuilder();
		//获取当前的url
		HttpUrl oldUrl = originalRequest.url();
		LogManager.i(TAG, "intercept:------------oldUrl---------->" + oldUrl);
		
		//获取头信息的集合如：jeapp ,njeapp ,mall
		List<String> urlnameList = originalRequest.headers("urlname");
		if (urlnameList != null && urlnameList.size() > 0) {
			//删除原有配置中的值,就是namesAndValues集合里的值
			builder.removeHeader("urlname");
			//获取头信息中配置的value,如：manage或者mdffx
			String urlname = urlnameList.get(0);
			LogManager.i(TAG, "intercept:-------urlname------" + urlname);
			HttpUrl baseURL = null;
			//根据头信息中配置的value,来匹配新的base_url地址
			if (!TextUtils.isEmpty(urlname)) {
				switch (urlname) {
					case ConstantData.TO_FIRST_PAGR_FLAG:
						baseURL = HttpUrl.parse(ConstantData.TO_FIRST_PAGR_URL);
						break;
					case ConstantData.TO_PROJECT_FLAG:
						baseURL = HttpUrl.parse(ConstantData.TO_PROJECT_URL);
						break;
					//                    case ConstantData.TO_PROJECT_FLAG:
					//                        baseURL = HttpUrl.parse(ConstantData.TO_PROJECT_URL);
					//                        break;
					case ConstantData.TO_RESOURCE_FLAG:
						baseURL = HttpUrl.parse(ConstantData.TO_RESOURCE_URL);
						break;
					case ConstantData.TO_MINE_FLAG:
						baseURL = HttpUrl.parse(ConstantData.TO_MINE_URL);
						break;
					case ConstantData.TO_USER_DATA_FLAG:
						baseURL = HttpUrl.parse(ConstantData.TO_USER_DATA_URL);
						break;
					default:
						break;
				}
				//				if ("jeapp".equals(urlname)) {
				//					baseURL = HttpUrl.parse(ConstantData.PRE_URL);
				//				} else if ("njeapp".equals(urlname)) {
				//					baseURL = HttpUrl.parse(ConstantData.PRE_NEW_URL);
				//				} else if ("mall".equals(urlname)) {
				//					baseURL = HttpUrl.parse(ConstantData.MALL_URL);
				//				}
				
				LogManager.i(TAG, "intercept:-----oldUrl-----" + oldUrl.toString());
				//重建新的HttpUrl，需要重新设置的url部分
				HttpUrl newHttpUrl = oldUrl.newBuilder()
										 .scheme(baseURL.scheme())//http协议如：http或者https
										 .host(baseURL.host())//主机地址
										 .port(baseURL.port())//端口
										 .build();
				LogManager.i(TAG, "intercept:------scheme----" + baseURL.scheme());
				LogManager.i(TAG, "intercept:-----host-----" + baseURL.host());
				LogManager.i(TAG, "intercept:-----port-----" + baseURL.port());
				LogManager.i(TAG, "intercept:-----newHttpUrl-----" + newHttpUrl.toString());
				
				//获取处理后的新newRequest
				Request.Builder newBuilder = builder.url(newHttpUrl);
				
				Request newRequest = newBuilder.build();
				return chain.proceed(newRequest);
			}
			originalRequest = builder.build();
			return chain.proceed(originalRequest);
		} else {
			originalRequest = builder.build();
			return chain.proceed(originalRequest);
		}
		
	}
}
