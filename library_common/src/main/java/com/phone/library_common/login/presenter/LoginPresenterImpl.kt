package com.phone.library_common.login.presenter

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.phone.library_common.BaseApplication
import com.phone.library_common.R
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.BaseResponse
import com.phone.library_common.base.IBaseView
import com.phone.library_common.bean.UserBean
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.login.bean.GetVerificationCode
import com.phone.library_common.login.bean.LoginResponse
import com.phone.library_common.manager.LogManager.i
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.library_common.manager.SharedPreferencesManager
import com.phone.library_common.manager.UserBeanDaoManager
import com.phone.library_common.login.model.ILoginModel
import com.phone.library_common.login.model.LoginModelImpl
import com.phone.library_common.login.view.ILoginView
import com.phone.library_common.login.view.IRegisterView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import java.util.*

/**
 * author    : Urasaki
 * e-mail    : 1164688204@qq.com
 * date      : 2019/3/10 17:04
 * introduce : Presenter登录模块的（一个小模块对应一个Presenter）
 */

class LoginPresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), ILoginPresenter {

    private val TAG = LoginPresenterImpl::class.java.simpleName

    //    private IResourceChildView loginView;//P需要与V 交互，所以需要持有V的引用
    private var model: ILoginModel

    init {
        attachView(baseView)
        model = LoginModelImpl()
    }

    override fun getAuthCode(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is ILoginView) {
                val loginView = baseView
                loginView.showLoading()
                RetrofitManager.instance()
                    .responseString(
                        rxAppCompatActivity,
                        model.getAuthCode(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                i(TAG, "success*****$success")
                                val baseResponse = JSON.parseObject(
                                    success,
                                    BaseResponse::class.java
                                )
                                if (baseResponse.code == 0) {
                                    val getVerificationCode = JSON.parseObject(
                                        success,
                                        GetVerificationCode::class.java
                                    )
                                    loginView.getAuthCodeSuccess(getVerificationCode.data)
                                } else {
                                    loginView.getAuthCodeError(
                                        BaseApplication.instance().resources.getString(
                                            R.string.library_data_in_wrong_format
                                        )
                                    )
                                }
                                loginView.hideLoading()
                            }

                            override fun onError(error: String) {
                                i(TAG, "error*****$error")
                                // 异常处理
                                loginView.getAuthCodeError(
                                    BaseApplication.instance().resources.getString(
                                        R.string.library_request_was_aborted
                                    )
                                )
                                loginView.hideLoading()
                            }
                        })
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
                //                                    BaseApplication mineApplication = BaseApplication.instance();
                //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                //                                    loginView.loginSuccess(loginResponse.getData());
                //                                } else {
                //                                    loginView.loginError(BaseApplication.instance().getResources().getString(R.string.library_data_in_wrong_format));
                //                                }
                //                                loginView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                loginView.loginError(BaseApplication.instance().getResources().getString(R.string.library_request_was_aborted));
                //                                loginView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    override fun loginWithAuthCode(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is ILoginView) {
                val loginView = baseView
                Objects.requireNonNull(RetrofitManager.instance())
                    .responseString(
                        rxAppCompatActivity,
                        model.loginWithAuthCode(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                i(TAG, "success*****$success")
                                val baseResponse = JSON.parseObject(
                                    success,
                                    BaseResponse::class.java
                                )
                                if (baseResponse.code == 0) {
                                    val loginResponse = JSON.parseObject(
                                        success,
                                        LoginResponse::class.java
                                    )
                                    SharedPreferencesManager.put(
                                        "accessToken",
                                        loginResponse.data.token
                                    )
                                    SharedPreferencesManager.put("isLogin", true)
                                    loginView.loginWithAuthCodeSuccess(loginResponse.data)
                                } else {
                                    loginView.loginWithAuthCodeError(
                                        BaseApplication.instance().resources.getString(
                                            R.string.library_data_in_wrong_format
                                        )
                                    )
                                }
                                loginView.hideLoading()
                            }

                            override fun onError(error: String) {
                                i(TAG, "error*****$error")
                                // 异常处理
                                loginView.loginWithAuthCodeError(
                                    BaseApplication.instance().resources.getString(
                                        R.string.library_request_was_aborted
                                    )
                                )
                                loginView.hideLoading()
                            }
                        })
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
                //                                    BaseApplication mineApplication = BaseApplication.instance();
                //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
                //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
                //                                    loginView.loginSuccess(loginResponse.getData());
                //                                } else {
                //                                    loginView.loginError(BaseApplication.instance().getResources().getString(R.string.library_data_in_wrong_format));
                //                                }
                //                                loginView.hideLoading();
                //                            }
                //                        }, new Consumer<Throwable>() {
                //                            @Override
                //                            public void accept(Throwable throwable) throws Exception {
                //                                LogManager.i(TAG, "throwable*****" + throwable.getMessage());
                //                                // 异常处理
                //                                loginView.loginError(BaseApplication.instance().getResources().getString(R.string.library_request_was_aborted));
                //                                loginView.hideLoading();
                //                            }
                //                        });
            }
        }
    }

    override fun login(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is ILoginView) {
                val loginView = baseView
                val userId = bodyParams["userId"]
                val password = bodyParams["password"]
                val userBeanDaoManager = UserBeanDaoManager()
                val userBeanList = userId?.let { userBeanDaoManager.queryByQueryBuilder(userId) }
                if (userBeanList != null && userBeanList.size > 0) {
                    val userBean = userBeanList[0]
                    if (userBean.userId == userId) {
                        if (password == userBean.password) {
                            loginView.loginSuccess("")
                        } else {
                            loginView.loginError(ResourcesManager.getString(R.string.library_please_enter_the_correct_password))
                        }
                    } else {
                        loginView.loginError(ResourcesManager.getString(R.string.library_this_user_cannot_be_found))
                    }
                } else {
                    loginView.loginError(ResourcesManager.getString(R.string.library_this_user_cannot_be_found))
                }
            }
        }
    }

    override fun register(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IRegisterView) {
                val registerView = baseView
                registerView.showLoading()
                //                RetrofitManager.getInstance()
//                        .responseStringRxAppActivityBindToLifecycle(rxAppCompatActivity, model.register(bodyParams), new OnCommonSingleParamCallback<String>() {
//                            @Override
//                            public void onSuccess(String success) {
//                                LogManager.i(TAG, "success*****" + success);
//                                BaseResponse baseResponse = JSON.parseObject(success, BaseResponse.class);
//                                if (baseResponse.getCode() == 200) {
//                                    //                                    LoginResponse loginResponse = JSON.parseObject(success, LoginResponse.class);
//                                    //                                    BaseApplication mineApplication = BaseApplication.instance();
//                                    //                                    mineApplication.setShopId(loginResponse.getData().getShopId() + "");
//                                    //                                    mineApplication.setUserId(loginResponse.getData().getUserId() + "");
//                                    registerView.registerSuccess(success);
//                                } else {
//                                    registerView.registerError(BaseApplication.instance().getResources().getString(R.string.library_data_in_wrong_format));
//                                }
//                                registerView.hideLoading();
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                LogManager.i(TAG, "error*****" + error);
//                                // 异常处理
//                                registerView.registerError(BaseApplication.instance().getResources().getString(R.string.library_request_was_aborted));
//                                registerView.hideLoading();
//                            }
//                        });
                val userBeanDaoManager = UserBeanDaoManager()
                val userId = bodyParams["userId"]
                val password = bodyParams["password"]
                if (!TextUtils.isEmpty(userId)) {
                    if (!TextUtils.isEmpty(password)) {
                        val userBeanList =
                            userId?.let {
                                userBeanDaoManager.queryByQueryBuilder(userId)
                            }
                        if (userBeanList != null && userBeanList.size > 0 && userId == userBeanList[0].userId) {
                            registerView.registerError(ResourcesManager.getString(R.string.library_this_user_is_already_registered))
                        } else {
                            val userBean = UserBean()
                            userBean.userId = userId
                            userBean.password = password
                            userBeanDaoManager.insert(userBean)
                            registerView.registerSuccess("")
                        }
                    } else {
                        registerView.registerError(ResourcesManager.getString(R.string.library_please_input_a_password))
                    }
                } else {
                    registerView.registerError(ResourcesManager.getString(R.string.library_please_enter_one_user_name))
                }
                userBeanDaoManager.closeConnection()
                registerView.hideLoading()
            }
        }
    }
}