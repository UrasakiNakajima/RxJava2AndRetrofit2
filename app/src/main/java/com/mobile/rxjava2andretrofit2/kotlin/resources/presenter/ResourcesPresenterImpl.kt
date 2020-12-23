package com.mobile.rxjava2andretrofit2.kotlin.resources.presenter

import android.text.TextUtils
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.base.BasePresenter
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.kotlin.resources.model.ResourcesModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.resources.presenter.base.IResourcesPresenter
import com.mobile.rxjava2andretrofit2.kotlin.resources.view.IResourcesView
import com.mobile.rxjava2andretrofit2.kotlin.resources.bean.ResourcesBean
import com.mobile.rxjava2andretrofit2.manager.GsonManager

class ResourcesPresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IResourcesPresenter {

    private val TAG: String = "ResourcesPresenterImpl"
    //    private IResourcesView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: ResourcesModelImpl = ResourcesModelImpl();

    init {
        attachView(baseView)
    }

    override fun resourcesData(type: String, pageSize: String, currentPage: String) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IResourcesView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model.resourcesData(type, pageSize, currentPage), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, ResourcesBean::class.java)
                                    val response = GsonManager.getInstance().convert(success, ResourcesBean::class.java)
                                    baseView.resourcesDataSuccess(response.results)
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