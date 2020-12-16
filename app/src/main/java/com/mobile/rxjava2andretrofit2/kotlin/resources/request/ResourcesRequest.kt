package com.mobile.rxjava2andretrofit2.kotlin.mine.request

import com.mobile.rxjava2andretrofit2.common.ConstantData
import com.mobile.rxjava2andretrofit2.common.Url
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ResourcesRequest {

    @Headers(ConstantData.TO_RESOURCES_FLAG)
    @FormUrlEncoded
    @GET(Url.RESOURCES_URL)
    fun getResourcesData(): Observable<ResponseBody>

}