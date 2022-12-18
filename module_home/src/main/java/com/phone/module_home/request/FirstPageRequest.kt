package com.phone.module_home.request

import com.phone.library_common.common.ConstantData
import com.phone.library_common.common.ConstantUrl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface FirstPageRequest {

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_URL)
    fun getFirstPage(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    @Headers("urlname:" + ConstantData.TO_FIRST_PAGR_FLAG)
    @GET(ConstantUrl.FIRST_PAGE_DETAILS_URL)
    fun getFirstPageDetails(@QueryMap bodyParams: Map<String, String>): Observable<ResponseBody>

    //    @FormUrlEncoded
    //    @POST(ConstantUrl.FIRST_PAGE_URL)
    //    Observable<FirstPageResponse.QuestionBean> getFirstPageData(@Body RequestBody requestBody);

}