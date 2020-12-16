package com.mobile.rxjava2andretrofit2.kotlin.resources.presenter

import com.google.gson.Gson
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.base.BasePresenter
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.mine.bean.MineResponse
import com.mobile.rxjava2andretrofit2.kotlin.resources.model.ResourcesModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.resources.presenter.base.IResourcesPresenter
import com.mobile.rxjava2andretrofit2.kotlin.mine.view.IResourcesView

class ResourcesPresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IResourcesPresenter {

    private val TAG: String = "MinePresenterImpl"
    //    private IResourcesView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: ResourcesModelImpl? = null;

    init {
        attachView(baseView)
        model = ResourcesModelImpl()
    }

    override fun resourcesData() {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IResourcesView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model!!.resourcesData(), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!success.isEmpty()) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
                                    val gson = Gson()
                                    val response = gson.fromJson(success, MineResponse::class.java)
                                    baseView.resourcesDataSuccess(response.ans_list)
                                } else {
                                    baseView.resourcesDataError(MineApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.resourcesDataError(error)
                                baseView.hideLoading()
                            }
                        })
                disposableList.add(disposable)
            }
        }
    }

}