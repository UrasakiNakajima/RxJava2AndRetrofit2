package com.phone.module_home.model

import com.phone.library_network.bean.ApiResponse2
import com.phone.library_common.bean.ResultData
import com.phone.library_network.manager.RetrofitManager
import com.phone.module_home.request.HomePageRequest

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:03
 * introduce :
 */

class HomeModelImpl : IHomeModel {

    private val TAG = HomeModelImpl::class.java.simpleName

    override suspend fun homePage(bodyParams: Map<String, String>): ApiResponse2<ResultData> {
        return RetrofitManager.instance.mRetrofit
            .create(HomePageRequest::class.java)
            .getHomePage(bodyParams)
    }

}