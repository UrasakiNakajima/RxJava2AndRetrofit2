package com.phone.module_home.presenter

import android.text.TextUtils
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.FirstPageResponse
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager.i
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_home.R
import com.phone.module_home.model.FirstPageModelImpl
import com.phone.module_home.view.IFirstPageView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:05
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class FirstPagePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(),
    IFirstPagePresenter {

    private val TAG = "FirstPagePresenterImpl"

    //    private IFirstPageView firstPageView;//P需要与V 交互，所以需要持有V的引用
    private var model: FirstPageModelImpl

    init {
        attachView(baseView)
        model = FirstPageModelImpl()
    }

    override fun firstPage(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IFirstPageView) {
                val firstPageView = baseView
                //                firstPageView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                RetrofitManager.instance()
                    .responseString2(
                        rxFragment,
                        model.firstPage(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    val response = GsonManager().convert(
                                        success,
                                        FirstPageResponse::class.java
                                    )

                                    //											 String jsonStr = new GsonManager().toJson(response);
                                    if (response.error_code == 0) {
                                        firstPageView.firstPageDataSuccess(
                                            response.result?.data ?: mutableListOf()
                                        )
                                    } else {
                                        firstPageView.firstPageDataError(
                                            response.reason ?: ResourcesManager.getString(
                                                R.string.loading_failed
                                            )
                                        )
                                    }
                                } else {
                                    firstPageView.firstPageDataError(
                                        ResourcesManager.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                            }

                            override fun onError(error: String) {
                                i(TAG, "error*****$error")
                                firstPageView.firstPageDataError(error)
                            }
                        })
                //				compositeDisposable.add(disposable);

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.firstPageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    firstPageView.firstPageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    firstPageView.firstPageDataError(BaseApplication.instance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                firstPageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                firstPageView.firstPageDataError(BaseApplication.instance().getResources().getString(R.string.request_was_aborted));
                //                                firstPageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    override fun firstPage2(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IFirstPageView) {
                val firstPageView = baseView
                //                firstPageView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                RetrofitManager.instance()
                    .responseString(
                        rxAppCompatActivity,
                        model.firstPage(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {

                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    val response = GsonManager().convert(
                                        success,
                                        FirstPageResponse::class.java
                                    )
                                    //											 String jsonStr = new GsonManager().toJson(response);
                                    if (response.error_code == 0) {
                                        firstPageView.firstPageDataSuccess(
                                            response.result?.data ?: mutableListOf()
                                        )
                                    } else {
                                        firstPageView.firstPageDataError(
                                            response.reason ?: ResourcesManager.getString(
                                                R.string.loading_failed
                                            )
                                        )
                                    }
                                } else {
                                    firstPageView.firstPageDataError(
                                        ResourcesManager.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                            }

                            override fun onError(error: String) {
                                i(TAG, "error*****$error")
                                firstPageView.firstPageDataError(error)
                            }
                        })
                //				compositeDisposable.add(disposable);

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.firstPageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    firstPageView.firstPageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    firstPageView.firstPageDataError(BaseApplication.instance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                firstPageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                firstPageView.firstPageDataError(BaseApplication.instance().getResources().getString(R.string.request_was_aborted));
                //                                firstPageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

}