package com.mobile.rxjava2andretrofit2.first_page.request;

import com.mobile.common_library.common.ConstantData;
import com.mobile.common_library.common.ConstantUrl;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface FirstPageRequest {

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @FormUrlEncoded
    @POST(ConstantUrl.FIRST_PAGE_URL)
    Observable<ResponseBody> getFirstPage(@FieldMap Map<String, String> bodyParams);

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
    Observable<ResponseBody> getFirstPageDetails(@QueryMap Map<String, String> bodyParams);

//    @FormUrlEncoded
//    @POST(ConstantUrl.FIRST_PAGE_URL)
//    Observable<FirstPageResponse.QuestionBean> getFirstPageData(@FieldMap Map<String, String> bodyParams);


}
