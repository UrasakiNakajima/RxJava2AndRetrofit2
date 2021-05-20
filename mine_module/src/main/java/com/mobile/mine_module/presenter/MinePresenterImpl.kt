package com.mobile.mine_module.presenter

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSONObject
import com.mobile.common_library.BaseApplication
import com.mobile.common_library.base.BasePresenter
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.OnCommonSingleParamCallback
import com.mobile.common_library.manager.GsonManager
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.mine_module.R
import com.mobile.mine_module.bean.MineDetailsResponse
import com.mobile.mine_module.bean.MineResponse
import com.mobile.mine_module.model.MineModelImpl
import com.mobile.mine_module.view.IMineDetailsView
import com.mobile.mine_module.view.IMineView
import com.mobile.mine_module.view.IUserDataView

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    private val TAG: String = "MinePresenterImpl"

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: MineModelImpl = MineModelImpl();

    init {
        attachView(baseView)
    }

    override fun mineData(fragment: Fragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(fragment, model.mineData(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)
                                    if (response.ans_list != null && response.ans_list.size > 0) {
                                        baseView.mineDataSuccess(response.ans_list)
                                    } else {
                                        baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                                    }
                                } else {
                                    baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
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
                        .responseString(model.mineDetails(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineDetails success*****$success")
                                if (!TextUtils.isEmpty(success)) {
                                    val response: MineDetailsResponse = JSONObject.parseObject(success, MineDetailsResponse::class.java);
                                    LogManager.i(TAG, "success data*****$response")
                                    if (response.data != null && response.data!!.size > 0) {
                                        baseView.mineDetailsSuccess(response.data!!)
                                    } else {
                                        baseView.mineDetailsError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
                                    }
                                } else {
                                    baseView.mineDetailsError(BaseApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.mineDetailsError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)

                //                //okhttp3请求（响应速度稍慢，可改进）
                //                Okhttp3Manager.getInstance()
                //                        .postAsyncKeyValuePairsOkhttp3(ConstantUrl.BASE_URL + ConstantUrl.FIRST_PAGE_DETAILS_URL,
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

    override fun userData(appCompatActivity: AppCompatActivity, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(appCompatActivity, model.userData(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(BaseApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.userDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }

    override fun userData(appCompatActivity: AppCompatActivity, token: String, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(appCompatActivity, model.userData(token, bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.getInstance().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(BaseApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.userDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }
}