package com.phone.module_home.presenter

import android.text.TextUtils
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.HomePageResponse
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_home.R
import com.phone.module_home.model.HomeModelImpl
import com.phone.module_home.view.IHomePageView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:05
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class HomePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(),
    IHomePresenter {

    private val TAG = "HomePresenterImpl"

    //    private IFirstPageView homePageView;//P需要与V 交互，所以需要持有V的引用
    private var model: HomeModelImpl
    private val jobList = mutableListOf<Job>()

    init {
        attachView(baseView)
        model = HomeModelImpl()
    }

    override fun homePage(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IHomePageView) {
                val homePageView = baseView
                //rxjava2+retrofit2请求（响应速度更快）

                LogManager.i(TAG, "homePage thread name*****${Thread.currentThread().name}")

                val job = GlobalScope.launch {
                    //开启GlobalScope.launch这种协程之后就是在线程执行了
                    val homePageList = homePageSuspend(bodyParams)
                    LogManager.i(TAG, "homePage2 thread name*****${Thread.currentThread().name}")

                    withContext(Dispatchers.Main) {
                        //然后切换到主线程
                        LogManager.i(
                            TAG,
                            "withContext thread name*****${Thread.currentThread().name}"
                        )

                        if (homePageList.size > 0) {
                            homePageView.homePageDataSuccess(homePageList)
                        } else {
                            homePageView.homePageDataError(
                                ResourcesManager.getString(
                                    R.string.loading_failed
                                )
                            )
                        }
                    }
                }
                jobList.add(job)

//                RetrofitManager.instance()
//                    .responseString2(
//                        rxFragment,
//                        model.homePage(bodyParams),
//                        object : OnCommonSingleParamCallback<String> {
//                            override fun onSuccess(success: String) {
//                                i(TAG, "success*****$success")
//                                if (!TextUtils.isEmpty(success)) {
//                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
//                                    val response = GsonManager().convert(
//                                        success,
//                                        FirstPageResponse::class.java
//                                    )
//
//                                    //											 String jsonStr = new GsonManager().toJson(response);
//                                    if (response.error_code == 0) {
//                                        homePageView.homePageDataSuccess(
//                                            response.result?.data ?: mutableListOf()
//                                        )
//                                    } else {
//                                        homePageView.homePageDataError(
//                                            response.reason ?: ResourcesManager.getString(
//                                                R.string.loading_failed
//                                            )
//                                        )
//                                    }
//                                } else {
//                                    homePageView.homePageDataError(
//                                        ResourcesManager.getString(
//                                            R.string.loading_failed
//                                        )
//                                    )
//                                }
//                            }
//
//                            override fun onError(error: String) {
//                                i(TAG, "error*****$error")
//                                homePageView.homePageDataError(error)
//                            }
//                        })

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.homePageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse homePageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    homePageView.homePageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    homePageView.homePageDataError(BaseApplication.instance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                homePageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                homePageView.homePageDataError(BaseApplication.instance().getResources().getString(R.string.request_was_aborted));
                //                                homePageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    suspend fun homePageSuspend(bodyParams: Map<String, String>): List<HomePageResponse.ResultData.JuheNewsBean> {
        val homePageList = mutableListOf<HomePageResponse.ResultData.JuheNewsBean>()
        withContext(Dispatchers.IO) {
            //切换到IO线程
            val success = model.homePage2(bodyParams).execute().body()?.string()
            success?.let {
                //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                val response = GsonManager().convert(
                    success,
                    HomePageResponse::class.java
                )
                //											 String jsonStr = new GsonManager().toJson(response);
                if (response.error_code == 0) {
                    val responseData = response.result?.data ?: mutableListOf()
                    if (responseData.size > 0) {
                        homePageList.clear()
                        homePageList.addAll(responseData)
                    }
                }
            }
        }
        return homePageList
    }

    override fun homePage2(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IHomePageView) {
                val homePageView = baseView
                //                homePageView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                RetrofitManager.instance()
                    .responseString(
                        rxAppCompatActivity,
                        model.homePage(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {

                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    val response = GsonManager().convert(
                                        success,
                                        HomePageResponse::class.java
                                    )
                                    //											 String jsonStr = new GsonManager().toJson(response);
                                    if (response.error_code == 0) {
                                        homePageView.homePageDataSuccess(
                                            response.result?.data ?: mutableListOf()
                                        )
                                    } else {
                                        homePageView.homePageDataError(
                                            response.reason ?: ResourcesManager.getString(
                                                R.string.loading_failed
                                            )
                                        )
                                    }
                                } else {
                                    homePageView.homePageDataError(
                                        ResourcesManager.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                homePageView.homePageDataError(error)
                            }
                        })
                //				compositeDisposable.add(disposable);

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.homePageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse homePageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    homePageView.homePageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    homePageView.homePageDataError(BaseApplication.instance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                homePageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                homePageView.homePageDataError(BaseApplication.instance().getResources().getString(R.string.request_was_aborted));
                //                                homePageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    override fun detachView() {
        super.detachView()
        for (i in 0..jobList.size - 1) {
            if (jobList.get(i).isActive) {
                jobList.get(i).cancel()
            }
        }
    }

}