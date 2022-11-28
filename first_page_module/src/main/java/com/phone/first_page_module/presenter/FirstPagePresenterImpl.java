package com.phone.first_page_module.presenter;

import android.text.TextUtils;

import com.phone.common_library.BaseApplication;
import com.phone.common_library.base.BasePresenter;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.bean.FirstPageResponse;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.GsonManager;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.first_page_module.R;
import com.phone.first_page_module.model.FirstPageModelImpl;
import com.phone.first_page_module.view.IFirstPageView;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle3.components.support.RxFragment;

import java.util.Map;
import java.util.Objects;

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:05
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class FirstPagePresenterImpl extends BasePresenter<IBaseView>
        implements IFirstPagePresenter {

    private static final String TAG = "FirstPagePresenterImpl";
    //    private IFirstPageView firstPageView;//P需要与V 交互，所以需要持有V的引用
    private final FirstPageModelImpl model;

    public FirstPagePresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new FirstPageModelImpl();
    }

    @Override
    public void firstPage(RxFragment fragment, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IFirstPageView) {
                IFirstPageView firstPageView = (IFirstPageView) baseView;
                //                firstPageView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                Objects.requireNonNull(RetrofitManager.Companion.get())
                        .responseString2(fragment, model.firstPage(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.INSTANCE.i(TAG, "success*****" + success);
                                if (!TextUtils.isEmpty(success)) {
                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    FirstPageResponse response = new GsonManager().convert(success, FirstPageResponse.class);

                                    //											 String jsonStr = new GsonManager().toJson(response);
                                    if (response.getError_code() == 0) {
                                        firstPageView.firstPageDataSuccess(response.getResult().getData());
                                    } else {
                                        firstPageView.firstPageDataError(response.getReason());
                                    }
                                } else {
                                    firstPageView.firstPageDataError(BaseApplication.Companion.get().getResources().getString(R.string.loading_failed));
                                }
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.INSTANCE.i(TAG, "error*****" + error);
                                firstPageView.firstPageDataError(error);
                            }
                        });
                //				compositeDisposable.add(disposable);

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.firstPageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.INSTANCE.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    firstPageView.firstPageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    firstPageView.firstPageDataError(BaseApplication.get().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                firstPageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.INSTANCE.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                firstPageView.firstPageDataError(BaseApplication.get().getResources().getString(R.string.request_was_aborted));
                //                                firstPageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    @Override
    public void firstPage2(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IFirstPageView) {
                IFirstPageView firstPageView = (IFirstPageView) baseView;
                //                firstPageView.showLoading();
                //rxjava2+retrofit2请求（响应速度更快）
                Objects.requireNonNull(RetrofitManager.Companion.get())
                        .responseString(rxAppCompatActivity, model.firstPage(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.INSTANCE.i(TAG, "success*****" + success);
                                if (!TextUtils.isEmpty(success)) {

                                    //                                    FirstPageResponse response = JSONObject.parseObject(success, FirstPageResponse.class);
                                    FirstPageResponse response = new GsonManager().convert(success, FirstPageResponse.class);
                                    //											 String jsonStr = new GsonManager().toJson(response);
                                    if (response.getError_code() == 0) {
                                        firstPageView.firstPageDataSuccess(response.getResult().getData());
                                    } else {
                                        firstPageView.firstPageDataError(response.getReason());
                                    }
                                } else {
                                    firstPageView.firstPageDataError(BaseApplication.Companion.get().getResources().getString(R.string.loading_failed));
                                }
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.INSTANCE.i(TAG, "error*****" + error);
                                firstPageView.firstPageDataError(error);
                            }
                        });
                //				compositeDisposable.add(disposable);

                ////                rxjava2+retrofit2请求（响应速度更快）
                //                disposable = model.firstPageData(bodyParams)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.INSTANCE.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                ////                                    FirstPageResponse firstPageResponse = JSON.parse(responseString, FirstPageResponse.class);
                //                                    firstPageView.firstPageDataSuccess(baseResponse.getMessage());
                //                                } else {
                //                                    firstPageView.firstPageDataError(BaseApplication.get().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                firstPageView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.INSTANCE.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                firstPageView.firstPageDataError(BaseApplication.get().getResources().getString(R.string.request_was_aborted));
                //                                firstPageView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

}
