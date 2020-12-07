package com.mobile.rxjava2andretrofit2.java.login.request;

import com.mobile.rxjava2andretrofit2.java.common.Url;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginRequest {

    @FormUrlEncoded
    @POST(Url.LOGIN_URL)
    Observable<ResponseBody> getLoginData(@FieldMap Map<String, String> bodyParams);

    @FormUrlEncoded
    @POST(Url.REGISTER_URL)
    Observable<ResponseBody> getRegisterData(@FieldMap Map<String, String> bodyParams);

//    @Multipart
//    @POST("upload")
//    Observable<JSONObject> addShopResult(@PartMap Map<String, RequestBody> fileMap,
//                                              @PartMap Map<String, List<RequestBody>> filesMap);

    @POST(Url.ADD_SHOP_URL)
    Observable<ResponseBody> addShopResult(@Body RequestBody requestBody);


}
