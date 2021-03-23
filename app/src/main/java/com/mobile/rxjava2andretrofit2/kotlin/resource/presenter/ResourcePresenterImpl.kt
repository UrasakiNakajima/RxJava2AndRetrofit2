package com.mobile.rxjava2andretrofit2.kotlin.resource.presenter

import android.text.TextUtils
import com.mobile.common_library.MineApplication
import com.mobile.common_library.base.BasePresenter
import com.mobile.common_library.base.IBaseView
import com.mobile.common_library.callback.OnCommonSingleParamCallback
import com.mobile.common_library.manager.GsonManager
import com.mobile.common_library.manager.LogManager
import com.mobile.common_library.manager.RetrofitManager
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.kotlin.resource.model.ResourceModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.resource.presenter.base.IResourcePresenter
import com.mobile.rxjava2andretrofit2.kotlin.resource.bean.ResourcesBean
import com.mobile.rxjava2andretrofit2.kotlin.resource.view.IResourceChildView

class ResourcePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IResourcePresenter {

    private val TAG: String = "ResourcePresenterImpl"
    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: ResourceModelImpl = ResourceModelImpl();

    init {
        attachView(baseView)
    }

    override fun resourceData(type: String, pageSize: String, currentPage: String) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IResourceChildView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model.resourceData(type, pageSize, currentPage), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, ResourcesBean::class.java)
                                    val response = GsonManager.getInstance().convert(success, ResourcesBean::class.java)
                                    if (response.results != null && response.results.size > 0) {
                                        LogManager.i(TAG, "response*****${response.toString()}")
                                        baseView.resourceDataSuccess(response.results)
                                    } else {
                                        baseView.resourceDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
                                    }
                                } else {
                                    baseView.resourceDataError(MineApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.resourceDataError(error)
                                baseView.hideLoading()
                            }
                        })
                compositeDisposable.add(disposable)
            }
        }
    }

}