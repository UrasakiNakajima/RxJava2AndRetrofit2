package com.phone.library_base.common

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2020/3/7 9:17
 * introduce :
 */

object ConstantUrl {

    //base地址（此地址失效，暂未替换，待以后替换）
    const val BASE_URL0 = "https://is.snssdk.net"

    //base地址
    const val BASE_URL = "http://v.juhe.cn"

    //base地址
    const val BASE_URL2 = "https://www.wanandroid.com"

    //base地址
    const val BASE_URL3 = "https://www.wanandroid.com"
    //    public static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";

    //    public static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";
    const val GET_VERIFICATION_CODE_URL = "/unifiedlogin/api/login/getCaptcha" //获取登录短信验证码
    const val LOGIN_WITH_AUTH_CODE_URL = "/unifiedlogin/api/login/sms" //短信登录
    const val REGISTER_URL = "/user/register"
    const val ADD_SHOP_URL = "/shop/register"
    const val USER_DATA = "/unifiedlogin/api/user/getDetails" //获取用户数据
    const val FIRST_PAGE_DETAILS_URL =
        "/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13"
    const val FIRST_PAGE_URL = "/toutiao/index"
    const val MINE_URL = "/toutiao/index"
    const val SUB_RESOURCE_URL = "/wxarticle/list/{id}/{pageNum}/json"
    const val SUB_PROJECT_URL = "/project/list/{pageNum}/json"
    const val SQUARE_URL = "/article/listproject/{currentPage}/json"
    const val PROJECT_TAB_URL = "/project/tree/json"
    const val RESOURCE_TAB_URL = "/wxarticle/chapters/json"

}