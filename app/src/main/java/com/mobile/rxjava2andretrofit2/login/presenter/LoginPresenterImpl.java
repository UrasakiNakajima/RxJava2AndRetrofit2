package com.mobile.rxjava2andretrofit2.login.presenter;

import com.alibaba.fastjson.JSON;
import com.mobile.rxjava2andretrofit2.MineApplication;
import com.mobile.rxjava2andretrofit2.R;
import com.mobile.rxjava2andretrofit2.base.BasePresenter;
import com.mobile.rxjava2andretrofit2.base.IBaseView;
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback;
import com.mobile.rxjava2andretrofit2.login.bean.LoginResponse;
import com.mobile.rxjava2andretrofit2.login.model.LoginModelImpl;
import com.mobile.rxjava2andretrofit2.login.presenter.base.ILoginPresenter;
import com.mobile.rxjava2andretrofit2.login.view.ILoginView;
import com.mobile.rxjava2andretrofit2.login.view.IAddShopView;
import com.mobile.rxjava2andretrofit2.login.view.IRegisterView;
import com.mobile.rxjava2andretrofit2.manager.LogManager;
import com.mobile.rxjava2andretrofit2.base.BaseResponse;
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * author    : xxxxxxxxxxx
 * e-mail    : xxxxxxxxxxx@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

public class LoginPresenterImpl extends BasePresenter<IBaseView>
        implements ILoginPresenter {

    private static final String TAG = "LoginPresenterImpl";
    //    private IMineView loginView;//P需要与V 交互，所以需要持有V的引用
    private LoginModelImpl model;

    public LoginPresenterImpl(IBaseView baseView) {
        attachView(baseView);
        model = new LoginModelImpl();
    }

    @Override
    public void login(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof ILoginView) {
                ILoginView loginView = (ILoginView) baseView;
                loginView.showLoading();
                disposable = RetrofitManager.getInstance()
                        .responseString(model.login(bodyParams), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
                                if (baseResponse.getCode() == 200) {
                                    LoginResponse loginResponse = JSON.parseObject(success, LoginResponse.class);
                                    MineApplication mineApplication = MineApplication.getInstance();
                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                                    loginView.loginSuccess(loginResponse.getData());
                                } else {
                                    loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                                }
                                loginView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                // 异常处理
                                loginView.loginError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                                loginView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

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
    public void register(Map<String, String> bodyParams) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IRegisterView) {
                IRegisterView registerView = (IRegisterView) baseView;
                registerView.showLoading();
                disposable = RetrofitManager.getInstance()
                        .responseString(model.register(bodyParams), new OnCommonSingleParamCallback<String>() {
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
                disposableList.add(disposable);
            }
        }
    }

    @Override
    public void addShop(Map<String, String> bodyParams, Map<String, File> fileMap, Map<String, List<File>> filesMap) {
        IBaseView baseView = obtainView();
        if (baseView != null) {
            if (baseView instanceof IAddShopView) {
                IAddShopView addShopView = (IAddShopView) baseView;
                addShopView.showLoading();

                //构建请求体
                RequestBody requestBody = RetrofitManager.getInstance()
                        .multipartBody2(bodyParams, fileMap, filesMap);
                disposable = RetrofitManager.getInstance()
                        .responseString(model.addShop(requestBody), new OnCommonSingleParamCallback<String>() {
                            @Override
                            public void onSuccess(String success) {
                                LogManager.i(TAG, "success*****" + success);
                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
                                if (baseResponse.getCode() == 200) {
//                                    LoginResponse loginResponse = JSON.parseObject(responseString, LoginResponse.class);
//                                    MineApplication mineApplication = MineApplication.getInstance();
//                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
//                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
//                                    addShopView.addShopSuccess(loginResponse.getData());
                                } else {
                                    addShopView.addShopError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
                                }
                                addShopView.hideLoading();
                            }

                            @Override
                            public void onError(String error) {
                                LogManager.i(TAG, "error*****" + error);
                                // 异常处理
                                addShopView.addShopError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
                                addShopView.hideLoading();
                            }
                        });
                disposableList.add(disposable);

//                MediaType MEDIA_TYPE = MediaType.parse("image/*");
//                // form 表单形式上传
//                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//                //1.添加请求参数
//                //遍历map中所有参数到builder
//                if (bodyParams != null && bodyParams.size() > 0) {
//                    for (String key : bodyParams.keySet()) {
//                        if (bodyParams.get(key) != null && !"".equals(bodyParams.get(key))) {//如果参数不是null，才把参数传给后台
//                            multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
//                        }
//                    }
//                }
//
//                //遍历fileMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
//                if (fileMap != null && fileMap.size() > 0) {
//                    for (String key : fileMap.keySet()) {
//                        File file = fileMap.get(key);
//                        if (file != null && file.exists()) {//如果参数不是null，才把参数传给后台
//                            multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));
//
//                            LogManager.i(TAG, "file.getName()*****" + file.getName());
//                        }
//                    }
//                }
//
//                //遍历filesMap中所有图片绝对路径到builder，并约定key如"upload[]"作为php服务器接受多张图片的key
//                if (filesMap != null && filesMap.size() > 0) {
//                    for (String key : filesMap.keySet()) {
//                        List<File> files = filesMap.get(key);
//                        if (files != null) {//如果参数不是null，才把参数传给后台
//                            if (files != null && files.size() > 0) {
//                                for (int i = 0; i < files.size(); i++) {
//                                    if (files.get(i) != null && files.get(i).exists()) {
//                                        multipartBodyBuilder.addFormDataPart(key, files.get(i).getName(), RequestBody.create(MEDIA_TYPE, files.get(i)));
//
//                                        LogManager.i(TAG, "files.get(i).getName()*****" + files.get(i).getName());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                //构建请求体
//                RequestBody requestBody = multipartBodyBuilder.build();
//                disposable = model.addShop(requestBody)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<JSONObject>() {
//                            @Override
//                            public void accept(JSONObject jsonObject) throws Exception {
//                                String responseString = jsonObject.toJSONString();
//                                LogManager.i(TAG, "responseString*****" + responseString);
//                                BaseResponse baseResponse = JSON.parseObject(responseString, BaseResponse.class);
//                                if (baseResponse.getCode() == 200) {
////                                    LoginResponse loginResponse = JSON.parseObject(responseString, LoginResponse.class);
////                                    MineApplication mineApplication = MineApplication.getInstance();
////                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
////                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
////                                    addShopView.addShopSuccess(loginResponse.getData());
//                                } else {
//                                    addShopView.addShopError(MineApplication.getInstance().getResources().getString(R.string.data_in_wrong_format));
//                                }
//                                addShopView.hideLoading();
//                            }
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
//                                // 异常处理
//                                addShopView.addShopError(MineApplication.getInstance().getResources().getString(R.string.request_was_aborted));
//                                addShopView.hideLoading();
//                            }
//                        });
            }
        }
    }

}
