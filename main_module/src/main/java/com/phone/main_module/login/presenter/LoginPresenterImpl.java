package com.phone.main_module.login.presenter;

import com.alibaba.fastjson.JSON;
import com.phone.common_library.base.BasePresenter;
import com.phone.common_library.base.BaseResponse;
import com.phone.common_library.base.IBaseView;
import com.phone.common_library.callback.OnCommonSingleParamCallback;
import com.phone.common_library.manager.LogManager;
import com.phone.common_library.manager.RetrofitManager;
import com.phone.main_module.MineApplication;
import com.phone.main_module.R;
import com.phone.main_module.login.bean.GetVerificationCode;
import com.phone.main_module.login.bean.LoginResponse;
import com.phone.main_module.login.model.LoginModelImpl;
import com.phone.main_module.login.view.ILoginView;
import com.phone.main_module.login.view.IRegisterView;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.Map;

/**
 * author    : Urasaki
 * e-mail    : Urasaki@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class LoginPresenterImpl extends BasePresenter<IBaseView>
        implements ILoginPresenter {

    private static final String TAG = "LoginPresenterImpl";
    //    private IResourceChildView loginView;//P需要与V 交互，所以需要持有V的引用
    private LoginModelImpl model;

    public LoginPresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new LoginModelImpl();
    }

    @Override
    public void getAuthCode(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof ILoginView) {
                ILoginView loginView = (ILoginView) baseView;
                loginView.showLoading();
                RetrofitManager.getInstance()
                        .responseStringAutoDispose(rxAppCompatActivity, model.getAuthCode(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
                                if (baseResponse.getCode() == 0) {
                                    GetVerificationCode getVerificationCode = JSON.parseObject(success, GetVerificationCode.class);
                                    loginView.getAuthCodeSuccess(getVerificationCode.getData());
                                } else {
                                    loginView.getAuthCodeError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                                }
                                loginView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                // 异常处理
                                loginView.getAuthCodeError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                                loginView.hideLoading();
                            }
                        });
                //				compositeDisposable.add(disposable);

                //                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
                //                //2.通过RequestBody.create 创建requestBody对象
                //                RequestBody requestBody = RequestBody.create(mediaType, requestData);
                //                disposable = model.login(requestBody)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                //                                    LoginResponse loginResponse = JSON.parseObject(responseString, LoginResponse.class);
                //                                    MineApplication mineApplication = MineApplication.getInstance();
                //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                //                                    loginView.loginSuccess(loginResponse.getData());
                //                                } else {
                //                                    loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                loginView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                //                                loginView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    @Override
    public void loginWithAuthCode(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof ILoginView) {
                ILoginView loginView = (ILoginView) baseView;
                loginView.showLoading();
                RetrofitManager.getInstance()
                        .responseStringAutoDispose(rxAppCompatActivity, model.loginWithAuthCode(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
                                if (baseResponse.getCode() == 0) {
                                    LoginResponse loginResponse = JSON.parseObject(success, LoginResponse.class);
                                    MineApplication mineApplication = MineApplication.getInstance();
                                    //											 mineApplication.setUserId(loginResponse.getData().getUserId());
                                    mineApplication.setAccessToken(loginResponse.getData().getToken());
                                    mineApplication.setLogin(true);
                                    loginView.loginWithAuthCodeSuccess(loginResponse.getData());
                                } else {
                                    loginView.loginWithAuthCodeError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                                }
                                loginView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                // 异常处理
                                loginView.loginWithAuthCodeError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                                loginView.hideLoading();
                            }
                        });
                //				compositeDisposable.add(disposable);

                //                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
                //                //2.通过RequestBody.create 创建requestBody对象
                //                RequestBody requestBody = RequestBody.create(mediaType, requestData);
                //                disposable = model.login(requestBody)
                //                        .subscribeOn(Schedulers.io())
                //                        .observeOn(AndroidSchedulers.mainThread())
                //                        .subscribe(new Consumer<JSONObject>() {
                //                            @Override
                //                            public void accept(JSONObject jsonObject) throws Exception {
                //                                String responseString = jsonObject.toJSONString();
                //                                LogManager.i(TAG, "responseString*****" + responseString);
                //                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
                //                                if (baseResponse.getCode() == 200) {
                //                                    LoginResponse loginResponse = JSON.parseObject(responseString, LoginResponse.class);
                //                                    MineApplication mineApplication = MineApplication.getInstance();
                //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                //                                    loginView.loginSuccess(loginResponse.getData());
                //                                } else {
                //                                    loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                //                                }
                //                                loginView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                //                                loginView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    @Override
    public void register(RxAppCompatActivity rxAppCompatActivity, Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IRegisterView) {
                IRegisterView registerView = (IRegisterView) baseView;
                registerView.showLoading();
                RetrofitManager.getInstance()
                        .responseStringRxAppActivityBindToLifecycle(rxAppCompatActivity, model.register(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
                                if (baseResponse.getCode() == 200) {
                                    //                                    LoginResponse loginResponse = JSON.parseObject(success, LoginResponse.class);
                                    //                                    MineApplication mineApplication = MineApplication.getInstance();
                                    //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                                    //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                                    registerView.registerSuccess(success);
                                } else {
                                    registerView.registerError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                                }
                                registerView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                // 异常处理
                                registerView.registerError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                                registerView.hideLoading();
                            }
                        });
                //				compositeDisposable.add(disposable);
            }
        }
    }

}
