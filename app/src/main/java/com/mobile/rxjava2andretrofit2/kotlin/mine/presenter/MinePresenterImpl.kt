package com.mobile.rxjava2andretrofit2.kotlin.mine.presenter

import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.mobile.common_library.MineApplication
import com.mobile.common_library.base.BasePresenter
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.OnCommonSingleParamCallback
import com.mobile.common_library.manager.GsonManager
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineResponse
import com.mobile.rxjava2andretrofit2.kotlin.mine.model.MineModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IMineView

class MinePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IMinePresenter {

    private val TAG: String = "MinePresenterImpl"
    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: MineModelImpl = MineModelImpl();

    init {
        attachView(baseView)
    }

    override fun mineData(bodyParams: Map<String, String>) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IMineView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model.mineData(bodyParams), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    val response = GsonManager.getInstance().convert(success, MineResponse::class.java)
                                    if (response.ans_list != null && response.ans_list.size > 0) {
                                        baseView.mineDataSuccess(response.ans_list)
                                    } else {
                                        baseView.mineDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
                                    }
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
                compositeDisposable.add(disposable)
            }
        }
    }

}