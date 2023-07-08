package com.phone.library_login.request

import com.phone.library_common.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface LoginRequest {

    //    @FormUrlEncoded
    @GET(ConstantUrl.GET_VERIFICATION_CODE_URL)
    fun getAuthCode(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @FormUrlEncoded
    @POST(ConstantUrl.LOGIN_WITH_AUTH_CODE_URL)
    fun getLoginWithAuthCodeData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @FormUrlEncoded
    @POST(ConstantUrl.REGISTER_URL)
    fun getRegisterData(@FieldMap bodyParams: Map<String, String>): Observable<ResponseBody>

    //    @Multipart
    //    @POST("upload")
    //    Observable<JSONObject> addShopResult(@PartMap Map<String, RequestBody> fileMap,
    //                                              @PartMap Map<String, List<RequestBody>> filesMap);

    //    @Multipart
    //    @POST("upload")
    //    Observable<JSONObject> addShopResult(@PartMap Map<String, RequestBody> fileMap,
    //                                              @PartMap Map<String, List<RequestBody>> filesMap);

    @POST(ConstantUrl.ADD_SHOP_URL)
    fun addShopResult(@Body requestBody: RequestBody): Observable<ResponseBody>

}