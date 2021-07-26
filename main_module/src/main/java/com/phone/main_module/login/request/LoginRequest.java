package com.phone.main_module.login.request;

import com.phone.common_library.common.ConstantUrl;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface LoginRequest {
	
	//    @FormUrlEncoded
	@GET(ConstantUrl.GET_VERIFICATION_CODE_URL)
	Observable<ResponseBody> getAuthCode(@QueryMap Map<String, String> bodyParams);
	
	@FormUrlEncoded
	@POST(ConstantUrl.LOGIN_WITH_AUTH_CODE_URL)
	Observable<ResponseBody> getLoginWithAuthCodeData(@FieldMap Map<String, String> bodyParams);
	
	@FormUrlEncoded
	@POST(ConstantUrl.REGISTER_URL)
	Observable<ResponseBody> getRegisterData(@FieldMap Map<String, String> bodyParams);
	
	//    @Multipart
	//    @POST("upload")
	//    Observable<JSONObject> addShopResult(@PartMap Map<String, RequestBody> fileMap,
	//                                              @PartMap Map<String, List<RequestBody>> filesMap);
	
	@POST(ConstantUrl.ADD_SHOP_URL)
	Observable<ResponseBody> addShopResult(@Body RequestBody requestBody);
	
}
