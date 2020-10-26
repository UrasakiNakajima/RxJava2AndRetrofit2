package com.mobile.rxjava2andretrofit2.mine.request;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MineRequest {

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    Observable<ResponseBody> getMineData(@FieldMap Map<String, String> bodyParams);

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    Observable<ResponseBody> getFeedbackResult(@FieldMap Map<String, String> bodyParams);

}
