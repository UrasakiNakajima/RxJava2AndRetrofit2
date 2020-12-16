package com.mobile.rxjava2andretrofit2.kotlin.mine.presenter

import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.base.BasePresenter
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineDetailsResponse
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineResponse
import com.mobile.rxjava2andretrofit2.kotlin.mine.model.MineModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.mine.presenter.base.IMinePresenter
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineDetailsView
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineView

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    private val TAG: String = "MinePresenterImpl"
    //    private IResourcesView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: MineModelImpl? = null;

    init {
        attachView(baseView)
        model = MineModelImpl()
    }

    override fun mineData(bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model!!.mineData(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!success.isEmpty()) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
                                    val gson = Gson()
                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    baseView.mineDataSuccess(response.ans_list)
                                } else {
                                    baseView.mineDataError(MineApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDataError(error)
                                baseView.hideLoading()
                            }
                        })
                disposableList.add(disposable)
            }
        }
    }

    override fun mineDetails(bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineDetailsView) {
                baseView.showLoading()
                //rxjava2+retrofit2请求（响应速度更快）
                disposable = RetrofitManager.getInstance()
                        .responseString(model!!.mineDetails(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!success.isEmpty()) {
//                                    MineDetailsResponse response = JSONObject.parseObject(success, MineDetailsResponse.class);

                                    val response: MineDetailsResponse = JSONObject.parseObject(success, MineDetailsResponse::class.java);
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineDetailsResponse::class.java)
                                    LogManager.i(TAG, "success data*****$response")
                                    baseView.mineDetailsSuccess(response.data!!)
                                } else {
                                    baseView.mineDetailsError(MineApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDetailsError(error)
                                baseView.hideLoading()
                            }
                        })
                disposableList.add(disposable)

                //                //okhttp3请求（响应速度稍慢，可改进）
                //                Okhttp3Manager.getInstance()
                //                        .postAsyncKeyValuePairsOkhttp3(Url.BASE_URL + Url.FIRST_PAGE_DETAILS_URL,
                //                                bodyParams,
                //                                new OnCommonSingleParamCallback<String>() {
                //                                    @Override
                //                                    public void onSuccess(String success) {
                //                                        LogManager.i(TAG, "success2*****" + success);
                //                                    }
                //
                //                                    @Override
                //                                    public void onError(String error) {
                //                                        LogManager.i(TAG, "error2*****" + error);
                //                                    }
                //                                });

            }
        }
    }
}