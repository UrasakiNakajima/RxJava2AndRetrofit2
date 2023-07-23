package com.phone.library_login.presenter

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.phone.library_base.BaseApplication
import com.phone.library_common.R
import com.phone.library_mvp.BasePresenter
import com.phone.library_network.bean.BaseResponse
import com.phone.library_base.base.IBaseView
import com.phone.library_base.callback.OnCommonSingleParamCallback
import com.phone.library_greendao.bean.UserBean
import com.phone.library_login.bean.GetVerificationCode
import com.phone.library_login.bean.LoginResponse
import com.phone.library_base.manager.LogManager.i
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_base.manager.SharedPreferencesManager
import com.phone.library_network.manager.RetrofitManager
import com.phone.library_greendao.UserBeanDaoManager
import com.phone.library_login.model.ILoginModel
import com.phone.library_login.model.LoginModelImpl
import com.phone.library_login.view.ILoginView
import com.phone.library_login.view.IRegisterView
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

    //    private IBaseView baseView;//P需要与V 交互，所以需要持有V的引用
    private var model: ILoginModel

    init {
        attachView(baseView)
        model = LoginModelImpl()
    }

    override fun getAuthCode(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is ILoginView) {
                val loginView = baseView
                loginView.showLoading()
                RetrofitManager.instance().responseString(rxAppCompatActivity,
                    model.getAuthCode(bodyParams),
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            i(TAG, "success*****$success")
                            val baseResponse = JSON.parseObject(
                                success, BaseResponse::class.java
                            )
                            if (baseResponse.code == 0) {
                                val getVerificationCode = JSON.parseObject(
                                    success, GetVerificationCode::class.java
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
            }
        }
    }

    override fun loginWithAuthCode(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is ILoginView) {
                val loginView = baseView
                Objects.requireNonNull(RetrofitManager.instance()).responseString(
                    rxAppCompatActivity,
                    model.loginWithAuthCode(bodyParams),
                    object : OnCommonSingleParamCallback<String> {
                        override fun onSuccess(success: String) {
                            i(TAG, "success*****$success")
                            val baseResponse = JSON.parseObject(
                                success, BaseResponse::class.java
                            )
                            if (baseResponse.code == 0) {
                                val loginResponse = JSON.parseObject(
                                    success, LoginResponse::class.java
                                )
                                SharedPreferencesManager.put(
                                    "accessToken", loginResponse.data.token
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
            }
        }
    }

    override fun login(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        baseView?.let {
            if (it is ILoginView) {
                val loginView = it
                val userId = bodyParams["userId"]
                val password = bodyParams["password"]
                val userBeanDaoManager = UserBeanDaoManager()
                userId?.let {
                    val userBeanList = userBeanDaoManager.queryByQueryBuilder(userId)
                    if (userBeanList.size > 0) {
                        val userBean = userBeanList[0]
                        if (userBean.userId == userId) {
                            if (userBean.password.equals(password)) {
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
    }

    override fun register(
        rxAppCompatActivity: RxAppCompatActivity, bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        baseView?.let {
            if (it is IRegisterView) {
                val registerView = it
                val userId = bodyParams["userId"]
                val password = bodyParams["password"]
                if (!TextUtils.isEmpty(userId)) {
                    if (!TextUtils.isEmpty(password)) {
                        val userBeanDaoManager = UserBeanDaoManager()
                        val userBeanList = userId?.let {
                            userBeanDaoManager.queryByQueryBuilder(userId)
                        }
                        if (userBeanList != null && userBeanList.size > 0 && userId == userBeanList[0].userId) {
                            userBeanDaoManager.closeConnection()
                            registerView.registerError(ResourcesManager.getString(R.string.library_this_user_is_already_registered))
                        } else {
                            val userBean = UserBean()
                            userBean.userId = userId
                            userBean.password = password
                            userBeanDaoManager.insert(userBean)
                            userBeanDaoManager.closeConnection()
                            registerView.registerSuccess("")
                        }
                    } else {
                        registerView.registerError(ResourcesManager.getString(R.string.library_please_input_a_password))
                    }
                } else {
                    registerView.registerError(ResourcesManager.getString(R.string.library_please_enter_one_user_name))
                }
            }
        }
    }

}