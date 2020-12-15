package com.mobile.rxjava2andretrofit2.common;


/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2020/3/7 9:17
 * introduce :
 */


public class Url {

//    public static final String BASE_URL = "http://192.168.8.111:2602/km/";

    //正式地址
    public static final String BASE_URL = "http://is.snssdk.com";
//    public static final String BASE_URL = "http://baobab.kaiyanapp.com/api/";


    public static final String LOGIN_URL = "/user/login";
    public static final String REGISTER_URL = "/user/register";
    public static final String ADD_SHOP_URL = BASE_URL + "shop/register";

    public static final String SUBMIT_FEEDBACK_URL = BASE_URL + "problem/addProblem";

    public static final String FIRST_PAGE_URL = "/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781";
    public static final String MINE_URL = "/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781";
    public static final String FIRST_PAGE_DETAILS_URL = "/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13";


}
