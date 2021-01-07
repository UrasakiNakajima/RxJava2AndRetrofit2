package com.mobile.rxjava2andretrofit2.kotlin.project.view_model

import android.text.TextUtils
import com.mobile.rxjava2andretrofit2.MineApplication
import com.mobile.rxjava2andretrofit2.base.BaseViewModel
import com.mobile.rxjava2andretrofit2.callback.OnCommonSingleParamCallback
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.DataX
import com.mobile.rxjava2andretrofit2.kotlin.project.bean.ProjectBean
import com.mobile.rxjava2andretrofit2.kotlin.project.model.ProjectModelImpl
import com.mobile.rxjava2andretrofit2.manager.GsonManager
import com.mobile.rxjava2andretrofit2.manager.LogManager
import com.mobile.rxjava2andretrofit2.manager.RetrofitManager
import androidx.lifecycle.MutableLiveData
import com.mobile.rxjava2andretrofit2.R


class ProjectViewModelImpl() : BaseViewModel() {

    private val TAG: String = "ProjectViewModelImpl"
    private var model: ProjectModelImpl = ProjectModelImpl()
    //1.首先定义两个MutableLiveData的实例
    private val dataxSuccess: MutableLiveData<List<DataX>> = MutableLiveData()
    private val dataxError: MutableLiveData<String> = MutableLiveData()

    fun projectData(currentPage: String) {
        disposable = RetrofitManager
                .getInstance()
                .responseString(model.projectData(currentPage), object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: ProjectBean = GsonManager.getInstance().convert(success, ProjectBean::class.java)
                            if (response.data.datas != null && response.data.datas.size > 0) {
                                LogManager.i(TAG, "response*****${response.toString()}")

                                dataxSuccess.value = response.data.datas
                            } else {
                                dataxError.value = MineApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxError.value = MineApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxError.value = error
                    }
                })
    }

    fun getDataxSuccess(): MutableLiveData<List<DataX>> {
        return dataxSuccess
    }

    fun getDataxError(): MutableLiveData<String> {
        return dataxError
    }


}