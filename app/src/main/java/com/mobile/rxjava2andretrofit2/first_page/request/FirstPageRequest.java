package com.mobile.rxjava2andretrofit2.first_page.request;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FirstPageRequest {

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    Observable<ResponseBody> getFirstPageData(@FieldMap Map<String, String> bodyParams);
}
