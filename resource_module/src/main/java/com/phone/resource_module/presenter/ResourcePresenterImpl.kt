package com.phone.resource_module.presenter

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BasePresenter
import com.phone.common_library.base.IBaseView
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.resource_module.R
import com.phone.resource_module.bean.ResourcesBean
import com.phone.resource_module.model.ResourceModelImpl
import com.phone.resource_module.presenter.base.IResourcePresenter
import com.phone.resource_module.view.IResourceChildView

class ResourcePresenterImpl(baseView: IBaseView) : BasePresenter<IBaseView>(), IResourcePresenter {

    private val TAG: String = "ResourcePresenterImpl"

    //    private IResourceChildView feedbackView;//P需要与V 交互，所以需要持有V的引用
    private var model: ResourceModelImpl = ResourceModelImpl();

    init {
        attachView(baseView)
    }

    override fun resourceData(
        fragment: Fragment,
        type: String,
        pageSize: String,
        currentPage: String
    ) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IResourceChildView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                    .responseStringAutoDispose(
                        fragment,
                        model.resourceData(type, pageSize, currentPage),
                        object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
//                                    val response = JSONObject.parseObject(success, MineResponse::class.java)
//                                    val gson = Gson()
//                                    val response = gson.fromJson(success, ResourcesBean::class.java)
                                    val response = GsonManager.getInstance()
                                        .convert(success, ResourcesBean::class.java)
                                    if (response.results != null && response.results.size > 0) {
                                        LogManager.i(TAG, "response*****${response.toString()}")
                                        baseView.resourceDataSuccess(response.results)
                                    } else {
                                        baseView.resourceDataError(
                                            BaseApplication.getInstance().resources.getString(
                                                R.string.no_data_available
                                            )
                                        )
                                    }
                                } else {
                                    baseView.resourceDataError(
                                        BaseApplication.getInstance().resources.getString(
                                            R.string.loading_failed
                                        )
                                    )
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.resourceDataError(error)
                                baseView.hideLoading()
                            }
                        })
//                compositeDisposable.add(disposable)
            }
        }
    }

}