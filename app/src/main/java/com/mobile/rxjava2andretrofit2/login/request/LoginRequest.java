package com.mobile.rxjava2andretrofit2.login.request;

import com.alibaba.fastjson.JSONObject;
import com.mobile.rxjava2andretrofit2.common.Url;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRequest {

    @POST(Url.LOGIN_URL)
    Observable<ResponseBody> getLoginData(@Body RequestBody requestBody);

//    @Multipart
//    @POST("upload")
//    Observable<JSONObject> addShopResult(@PartMap Map<String, RequestBody> fileMap,
//                                              @PartMap Map<String, List<RequestBody>> filesMap);

    @POST(Url.ADD_SHOP_URL)
    Observable<ResponseBody> addShopResult(@Body RequestBody requestBody);


}
