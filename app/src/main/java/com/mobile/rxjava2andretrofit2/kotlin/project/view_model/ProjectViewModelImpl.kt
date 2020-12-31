package com.mobile.rxjava2andretrofit2.kotlin.project.view_model

import android.text.TextUtils
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.R
import com.mobile.rxjava2andretrofit2.base.BaseViewModel
import com.mobile.rxjava2andretrofit2.base.IBaseView
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.ProjectBean
import com.mobile.rxjava2andretrofit2.kotlin.project.model.ProjectModelImpl
import com.mobile.rxjava2andretrofit2.kotlin.project.view.IProjectChildView
import com.mobile.rxjava2andretrofit2.manager.GsonManager
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager

class ProjectViewModelImpl(baseView: IBaseView) : BaseViewModel() {

    private val TAG: String = "ProjectViewModelImpl"
    private var model: ProjectModelImpl = ProjectModelImpl()

    init {
        attachView(baseView)
    }

    fun getProjectData(currentPage: String) {
        val baseView = obtainView()
        if (baseView != null) {
            if (baseView is IProjectChildView) {
                baseView.showLoading()
                disposable = RetrofitManager.getInstance()
                        .responseString(model.projectData(currentPage), object : OnCommonSingleParamCallback<String> {
                            override fun onSuccess(success: String) {
                                LogManager.i(TAG, "success*****$success")
                                if (!TextUtils.isEmpty(success)) {
                                    val response = GsonManager.getInstance().convert(success, ProjectBean::class.java)
                                    if (response.data.datas != null && response.data.datas.size > 0) {
                                        LogManager.i(TAG, "response*****${response.toString()}")

                                        baseView.projectDataSuccess(response.data.datas)
                                    } else {
                                        baseView.projectDataError(MineApplication.getInstance().resources.getString(R.string.no_data_available))
                                    }
                                } else {
                                    baseView.projectDataError(MineApplication.getInstance().resources.getString(R.string.loading_failed))
                                }
                                baseView.hideLoading()
                            }

                            override fun onError(error: String) {
                                LogManager.i(TAG, "error*****$error")
                                baseView.projectDataError(error)
                                baseView.hideLoading()
                            }
                        })
                disposableList!!.add(disposable!!)
            }
        }
    }

}