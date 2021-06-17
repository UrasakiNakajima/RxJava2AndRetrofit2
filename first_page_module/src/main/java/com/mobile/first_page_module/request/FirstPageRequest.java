package com.mobile.first_page_module.request;

import com.mobile.common_library.common.ConstantData;
import com.mobile.common_library.common.ConstantUrl;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface FirstPageRequest {
	
	@Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
	@GET(ConstantUrl.FIRST_PAGE_URL)
	Observable<ResponseBody> getFirstPage(@QueryMap Map<String, String> bodyParams);
	
	@Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
	@GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
	Observable<ResponseBody> getFirstPageDetails(@QueryMap Map<String, String> bodyParams);
	
	//    @FormUrlEncoded
	//    @POST(ConstantUrl.FIRST_PAGE_URL)
	//    Observable<FirstPageResponse.QuestionBean> getFirstPageData(@Body RequestBody requestBody);
	
}
