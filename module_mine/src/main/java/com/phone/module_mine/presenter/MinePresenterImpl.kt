package com.phone.module_mine.presenter

import android.text.TextUtils
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BasePresenter
import com.phone.library_common.base.IBaseView
import com.phone.library_common.callback.OnCommonSingleParamCallback
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.RetrofitManager
import com.phone.module_mine.R
import com.phone.module_mine.bean.MineResponse
import com.phone.module_mine.model.MineModelImpl
import com.phone.module_mine.view.IMineView
import com.phone.module_mine.view.IUserDataView
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    companion object {
        private val TAG = MinePresenterImpl::class.java.simpleName
    }

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: MineModelImpl = MineModelImpl();

    init {
        attachView(baseView)
    }

    override fun mineData(rxFragment: RxFragment, bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                RetrofitManager.get()
                    .responseString7(
                        rxFragment,
                        model.mineData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager()
                                        .convert(success, MineResponse::class.java)

                                    if (response.error_code == 0) {
                                        baseView.mineDataSuccess(response.result.data)
                                    } else {
                                        baseView.mineDataError(response.reason)
                                    }
                                } else {
                                    baseView.mineDataError(
                                        BaseApplication.get().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
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

    override fun mineData2(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                RetrofitManager.get()
                    .responseString5(
                        rxAppCompatActivity,
                        model.mineData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "mineData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager()
                                        .convert(success, MineResponse::class.java)
//                                    String jsonStr = GsonManager . getInstance ().toJson(response);
                                    if (response.error_code == 0) {
                                        baseView.mineDataSuccess(response.result.data)
                                    } else {
                                        baseView.mineDataError(response.reason)
                                    }
                                } else {
                                    baseView.mineDataError(
                                        BaseApplication.get().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
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

    override fun userData(
        rxAppCompatActivity: RxAppCompatActivity,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                RetrofitManager.get()
                    .responseString5(
                        rxAppCompatActivity,
                        model.userData(bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = new GsonManager().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.get().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(
                                        BaseApplication.get().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
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

    override fun userData(
        rxAppCompatActivity: RxAppCompatActivity,
        accessToken: String,
        bodyParams: Map<String, String>
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IUserDataView) {
                baseView.showLoading()
                RetrofitManager.get()
                    .responseString5(
                        rxAppCompatActivity,
                        model.userData(accessToken, bodyParams),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "userData success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
//                                    val response = new GsonManager().convert(success, MineResponse::class.java)

//                                    if (response.ans_list != null && response.ans_list.size > 0) {
//                                        baseView.mineDataSuccess(response.ans_list)
//                                    } else {
//                                        baseView.mineDataError(BaseApplication.get().resources.getString(R.string.no_data_available))
//                                    }
                                } else {
                                    baseView.userDataError(
                                        BaseApplication.get().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
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