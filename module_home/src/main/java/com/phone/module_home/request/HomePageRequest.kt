package com.phone.module_home.request

import com.phone.library_network.bean.ApiResponse2
import com.phone.library_common.bean.ResultData
import com.phone.library_common.common.ConstantData
import com.phone.library_common.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface HomePageRequest {

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_URL)
    suspend fun getHomePage(@QueryMap bodyParams: Map<String, String>): ApiResponse2<ResultData>

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_URL)
    fun getHomePage2(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
    fun getHomePageDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    //    @FormUrlEncoded
    //    @POST(ConstantUrl.FIRST_PAGE_URL)
    //    Observable<FirstPageResponse.QuestionBean> getFirstPageData(@Body RequestBody requestBody);

}