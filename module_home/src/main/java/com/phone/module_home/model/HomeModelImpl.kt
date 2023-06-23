package com.phone.module_home.model

import com.phone.library_common.manager.RetrofitManager
import com.phone.module_home.request.HomePageRequest
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

class HomeModelImpl : IHomeModel {

    private val TAG = "HomeModelImpl"

    override fun homePage(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(HomePageRequest::class.java)
            .getHomePage(bodyParams)
    }

    override fun homePage2(bodyParams: Map<String, String>): Call<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(HomePageRequest::class.java)
            .getHomePage2(bodyParams)
    }

    override fun homePageDetails(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.instance().mRetrofit
            .create(HomePageRequest::class.java)
            .getHomePageDetails(bodyParams)
    }

    //    @Override
    //    public Observable<HomePageResponse.QuestionBean> homePageData(Map<String, String> bodyParams) {
    //        return RetrofitManager.instance().mRetrofit
    //                .create(HomePageRequest.class)
    //                .getHomePageData(bodyParams);
    //    }

}