package com.mobile.rxjava2andretrofit2.java.mine.presenter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.mobile.rxjava2andretrofit2.java.MineApplication;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.java.base.BasePresenter;
import com.mobile.rxjava2andretrofit2.java.base.IBaseView;
import com.mobile.rxjava2andretrofit2.java.callback.OnCommonSingleParamCallback;
import com.mobile.rxjava2andretrofit2.java.mine.bean.MineResponse;
import com.mobile.rxjava2andretrofit2.java.mine.model.MineModelImpl;
import com.mobile.rxjava2andretrofit2.java.mine.presenter.base.IMinePresenter;
import com.mobile.rxjava2andretrofit2.java.manager.LogManager;
import com.mobile.rxjava2andretrofit2.java.manager.RetrofitManager;
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineDetailsResponse;
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineDetailsView;
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineView;

import java.util.Map;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class MinePresenterImpl extends BasePresenter<IBaseView>
        implements IMinePresenter {

    private static final String TAG = "MinePresenterImpl";
    //    private IMineView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private MineModelImpl model;

    public MinePresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new MineModelImpl();
    }

    @Override
    public void mineData(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IMineView) {
                IMineView mineView = (IMineView) baseView;
                mineView.showLoading();
                disposable = RetrofitManager.getInstance()
                        .responseString(model.mineData(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                if (!success.isEmpty()) {
                                    MineResponse response = JSONObject.parseObject(success, MineResponse.class);
                                    mineView.mineDataSuccess(response.getAns_list());
                                } else {
                                    mineView.mineDataError(MineApplication.getInstance().getResources().getString(R.string.loading_failed));
                                }
                                mineView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                mineView.mineDataError(error);
                                mineView.hideLoading();
                            }
                        });
                disposableList.add(disposable);
            }
        }
    }

    @Override
    public void mineDetails(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IMineDetailsView) {
                IMineDetailsView mineDetailsView = (IMineDetailsView) baseView;
                mineDetailsView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                disposable = RetrofitManager.getInstance()
                        .responseString(model.mineDetails(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                if (!success.isEmpty()) {
//                                    MineDetailsResponse response = JSONObject.parseObject(success, MineDetailsResponse.class);
                                    Gson gson = new Gson();
                                    MineDetailsResponse response = gson.fromJson(success, MineDetailsResponse.class);
                                    LogManager.i(TAG, "success data*****" + response.getData().toString());
                                    mineDetailsView.mineDetailsSuccess(response.getData());
                                } else {
                                    mineDetailsView.mineDetailsError(MineApplication.getInstance().getResources().getString(R.string.loading_failed));
                                }
                                mineDetailsView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                mineDetailsView.mineDetailsError(error);
                                mineDetailsView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

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
