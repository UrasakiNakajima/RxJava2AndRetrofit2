package com.mobile.rxjava2andretrofit2.mine.presenter;

import com.alibaba.fastjson.JSONObject;
import com.mobile.rxjava2andretrofit2.base.BasePresenter;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback;
import com.mobile.rxjava2andretrofit2.mine.bean.MineResponse;
import com.mobile.rxjava2andretrofit2.mine.model.MineModelImpl;
import com.mobile.rxjava2andretrofit2.mine.presenter.base.IMinePresenter;
import com.mobile.rxjava2andretrofit2.mine.view.IFeedbackView;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;
import com.mobile.rxjava2andretrofit2.mine.view.IMineView;

import java.util.ArrayList;
import java.util.List;
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
                                    mineView.mineDataError("加载失败");
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
    public void submitFeedback(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IFeedbackView) {
                IFeedbackView feedbackView = (IFeedbackView) baseView;
                feedbackView.showLoading();
                disposable = RetrofitManager.getInstance()
                        .responseString(model.submitFeedback(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                feedbackView.submitFeedbackSuccess(success);
                                feedbackView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                feedbackView.submitFeedbackError(error);
                                feedbackView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

//                disposable = model.submitFeedback(bodyParams)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<JSONObject>() {
//                            @Override
//                            public void accept(JSONObject jsonObject) throws Exception {
//                                String responseString = jsonObject.toJSONString();
//                                LogManager.i(TAG, "responseString*****" + responseString);
//                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                if (baseResponse.getCode() == 200) {
//
////                                    feedbackView.submitFeedbackSuccess(feedbackResponse.getData());
//                                } else {
//                                    feedbackView.submitFeedbackError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
//                                }
//                                feedbackView.hideLoading();
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                                // 异常处理
//                                feedbackView.submitFeedbackError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                                feedbackView.hideLoading();
//                            }
//                        });
            }
        }
    }

}
