package com.mobile.rxjava2andretrofit2.first_page.request;

import com.mobile.rxjava2andretrofit2.common.Url;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface FirstPageRequest {

    @FormUrlEncoded
    @POST(Url.FIRST_PAGE_URL)
    Observable<ResponseBody> getFirstPageData(@FieldMap Map<String, String> bodyParams);

//    @GET(Url.FIRST_PAGE_URL)
//    Observable<ResponseBody> getFirstPageData(@QueryMap Map<String, String> bodyParams);

//    @FormUrlEncoded
//    @POST(Url.FIRST_PAGE_URL)
//    Observable<FirstPageResponse.QuestionBean> getFirstPageData(@FieldMap Map<String, String> bodyParams);


}
