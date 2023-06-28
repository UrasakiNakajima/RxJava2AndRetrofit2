package com.phone.module_home.presenter

import android.text.TextUtils
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_home.R
import com.phone.module_home.bean.HomePageResponse
import com.phone.module_home.model.HomeModelImpl
import com.phone.module_home.model.IHomeModel
import com.phone.module_home.view.IHomePageView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:05
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class HomePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IHomePresenter {

    private val TAG = "HomePresenterImpl"

    //    private IFirstPageView homePageView;//P需要与V 交互，所以需要持有V的引用
    private var model: IHomeModel
    private val mainScope = MainScope()

    init {
        attachView(baseView)
        model = HomeModelImpl()
    }

    override fun homePage(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IHomePageView) {
                val homePageView = baseView
                homePageView.showLoading()

                mainScope.launch {//开启MainScope这种协程之后就是在MAIN线程执行了
                    val apiResponse = execute { model.homePage(bodyParams) }
                    if (apiResponse.result != null && apiResponse.error_code == 0) {
                        val list = apiResponse.result?.data ?: mutableListOf()
                        if (list.size > 0) {
                            homePageView.homePageDataSuccess(
                                list
                            )
                        } else {
                            homePageView.homePageDataError(
                                ResourcesManager.getString(
                                    R.string.no_data_available
                                )
                            )
                        }
                    } else {
                        homePageView.homePageDataError(
                            apiResponse.reason ?: ResourcesManager.getString(
                                R.string.loading_failed
                            )
                        )
                    }
                    homePageView.hideLoading()
                    LogManager.i(TAG, "homePage2 thread name*****${Thread.currentThread().name}")
                }

            }
        }
    }

    override fun homePage2(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IHomePageView) {
                val homePageView = baseView
                homePageView.showLoading()
                //rxjava2+retrofit2请求（响应速度更快）
                RetrofitManager.instance().responseString(rxAppCompatActivity,
                    model.homePage2(bodyParams),
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            LogManager.i(TAG, "success*****$success")
                            if (!TextUtils.isEmpty(success)) {
                                val response = GsonManager().convert(
                                    success, HomePageResponse::class.java
                                )
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
                            homePageView.hideLoading()
                        }

                        override fun onError(error: String) {
                            LogManager.i(TAG, "error*****$error")
                            homePageView.homePageDataError(error)
                            homePageView.hideLoading()
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
        mainScope.cancel()
    }

}