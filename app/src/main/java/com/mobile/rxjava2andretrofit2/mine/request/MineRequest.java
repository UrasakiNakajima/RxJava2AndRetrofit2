package com.mobile.rxjava2andretrofit2.mine.request;

import com.mobile.rxjava2andretrofit2.common.Url;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface MineRequest {

    @FormUrlEncoded
    @POST(Url.MINE_URL)
    Observable<ResponseBody> getMineData(@FieldMap Map<String, String> bodyParams);

    @GET(Url.FIRST_PAGE_DETAILS_URL)
    Observable<ResponseBody> getMineDetails(@QueryMap Map<String, String> bodyParams);

    @FormUrlEncoded
    @POST("/wenda/v1/question/brow/?iid=10344168417&device_id=36394312781")
    Observable<ResponseBody> getFeedbackResult(@FieldMap Map<String, String> bodyParams);

}
