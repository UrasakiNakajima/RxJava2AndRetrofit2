package com.phone.common_library.common;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 9:17
 * introduce :
 */

public class ConstantUrl {
	
	//base地址（此地址失效，暂未替换，待以后替换）
	public static final String BASE_URL0 = "https://is.snssdk.net";

	//base地址
	public static final String BASE_URL  = "http://v.juhe.cn";
	//base地址
	public static final String BASE_URL2 = "https://www.wanandroid.com";
	//base地址
	public static final String BASE_URL3 = "https://wanandroid.com";
	//    public static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";
	
	public static final String GET_VERIFICATION_CODE_URL = "/unifiedlogin/api/login/getCaptcha";//获取登录短信验证码
	public static final String LOGIN_WITH_AUTH_CODE_URL  = "/unifiedlogin/api/login/sms";//短信登录
	public static final String REGISTER_URL              = "/user/register";
	public static final String ADD_SHOP_URL              = BASE_URL + "/shop/register";
	
	public static final String FIRST_PAGE_URL = "/toutiao/index";
	public static final String MINE_URL       = "/toutiao/index";
	public static final String USER_DATA      = "/unifiedlogin/api/user/getDetails";//获取用户数据
	
	public static final String FIRST_PAGE_DETAILS_URL = "/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13";
	
	public static final String RESOURCE_URL = "/api/data";
	
}
