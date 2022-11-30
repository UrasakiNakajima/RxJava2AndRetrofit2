package com.phone.module_home.model

import com.phone.common_library.manager.RetrofitManager
import com.phone.module_home.request.FirstPageRequest
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

class FirstPageModelImpl : IFirstPageModel {

    private val TAG = "FirstPageModelImpl"

    override fun firstPage(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(FirstPageRequest::class.java)
            .getFirstPage(bodyParams)
    }

    override fun firstPageDetails(bodyParams: Map<String, String>): Observable<ResponseBody> {
        return RetrofitManager.get().getRetrofit()
            .create(FirstPageRequest::class.java)
            .getFirstPageDetails(bodyParams)
    }

    //    @Override
    //    public Observable<FirstPageResponse.QuestionBean> firstPageData(Map<String, String> bodyParams) {
    //        return RetrofitManager.get().getRetrofit()
    //                .create(FirstPageRequest.class)
    //                .getFirstPageData(bodyParams);
    //    }

}