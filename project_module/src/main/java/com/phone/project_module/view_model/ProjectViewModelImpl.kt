package com.phone.project_module.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseViewModel
import com.phone.common_library.callback.OnCommonSingleParamCallback
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.RetrofitManager
import com.phone.common_library.manager.SingleLiveData
import com.phone.project_module.R
import com.phone.project_module.bean.DataX
import com.phone.project_module.bean.ProjectBean
import com.phone.project_module.model.ProjectModelImpl
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProjectViewModelImpl() : BaseViewModel(), IProjectViewModel {

    companion object {
        private val TAG: String = ProjectViewModelImpl::class.java.simpleName
    }

    private var model: ProjectModelImpl = ProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess: SingleLiveData<MutableList<DataX>> = SingleLiveData()
    val dataxRxFragmentError: SingleLiveData<String> = SingleLiveData()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivitySuccess: SingleLiveData<MutableList<DataX>> = SingleLiveData()
    val dataxRxActivityError: SingleLiveData<String> = SingleLiveData()

    override fun projectData(rxFragment: RxFragment, currentPage: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.projectData2(currentPage).execute().body()?.string()
                val response: ProjectBean =
                    GsonManager.getInstance().convert(success, ProjectBean::class.java)
                LogManager.i(TAG, "thread name*****${Thread.currentThread().name}")

                launch(Dispatchers.Main) {
                    response.data.let {
                        if (it.datas.size > 0) {
                            dataxRxFragmentSuccess.value = it.datas
                        } else {
                            dataxRxFragmentError.value =
                                BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                        }
                        LogManager.i(TAG, "thread2 name*****${Thread.currentThread().name}")
                    }
                }
            }
        }

//        RetrofitManager.getInstance()
//            .responseStringAutoDispose(
//                rxFragment,
//                model.projectData(currentPage),
//                object : OnCommonSingleParamCallback<String> {
//                    override fun onSuccess(success: String) {
//                        LogManager.i(TAG, "success*****$success")
//                        if (!TextUtils.isEmpty(success)) {
////                            val response2: ProjectBean = GsonManager.getInstance().convert(success, ProjectBean::class.java)
////                            response2.data.datas.get(0).author = null
////                            val jsonString: String = GsonManager.getInstance().toJson(response2)
////                            LogManager.i(TAG, "jsonString*****${jsonString}")
////                            val manager: ReadAndWriteManager = ReadAndWriteManager.getInstance()
////                            manager.writeExternal("mineLog.txt",
////                                    jsonString,
////                                    object : OnCommonSingleParamCallback<Boolean> {
////                                        override fun onSuccess(success: Boolean?) {
////                                            LogManager.i(TAG, "success*****" + success!!)
////                                            manager.unSubscribe()
////                                        }
////
////                                        override fun onError(error: String) {
////                                            LogManager.i(TAG, "error*****$error")
////                                            manager.unSubscribe()
////                                        }
////                                    })
////                            val response: ProjectBean = GsonManager.getInstance().convert(jsonString, ProjectBean::class.java)
//
//                            val response: ProjectBean =
//                                GsonManager.getInstance().convert(success, ProjectBean::class.java)
//                            if (response.data.datas != null && response.data.datas.size > 0) {
////                                LogManager.i(TAG, "response*****${response.toString()}")
//
//                                dataxRxFragmentSuccess.value = response.data.datas
//                            } else {
//                                dataxRxFragmentError.value =
//                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
//                            }
//                        } else {
//                            dataxRxFragmentError.value =
//                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
//                        }
//                    }
//
//                    override fun onError(error: String) {
//                        LogManager.i(TAG, "error*****$error")
//                        dataxRxFragmentError.value = error
//                    }
//                })
    }

    override fun projectData2(
        rxAppCompatActivity: RxAppCompatActivity,
        currentPage: String
    ) {
        RetrofitManager.getInstance()
            .responseString5(
                rxAppCompatActivity,
                model.projectData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
                            val response: ProjectBean =
                                GsonManager.getInstance().convert(success, ProjectBean::class.java)
                            if (response.data.datas.size > 0) {
                                dataxRxActivitySuccess.value = response.data.datas
                            } else {
                                dataxRxActivityError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxRxActivityError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxRxActivityError.value = error
                    }
                })
    }


}