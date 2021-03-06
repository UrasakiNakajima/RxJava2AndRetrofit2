package com.phone.project_module.view_model

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
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


class ProjectViewModelImpl() : BaseViewModel(), IProjectViewModel {

    companion object {
        private val TAG: String = ProjectViewModelImpl::class.java.simpleName
    }

    private var model: ProjectModelImpl = ProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    private val dataxRxFragmentSuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    private val dataxRxFragmentError: SingleLiveData<String> = SingleLiveData()

    //1.首先定义两个SingleLiveData的实例
    private val dataxRxAppCompatActivitySuccess: SingleLiveData<List<DataX>> = SingleLiveData()
    private val dataxRxAppCompatActivityError: SingleLiveData<String> = SingleLiveData()

    override fun projectDataRxFragment(rxFragment: RxFragment, currentPage: String) {
        RetrofitManager.getInstance()
            .responseStringAutoDispose(
                rxFragment,
                model.projectData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
//                            val response2: ProjectBean = GsonManager.getInstance().convert(success, ProjectBean::class.java)
//                            response2.data.datas.get(0).author = null
//                            val jsonString: String = GsonManager.getInstance().toJson(response2)
//                            LogManager.i(TAG, "jsonString*****${jsonString}")
//                            val manager: ReadAndWriteManager = ReadAndWriteManager.getInstance()
//                            manager.writeExternal("mineLog.txt",
//                                    jsonString,
//                                    object : OnCommonSingleParamCallback<Boolean> {
//                                        override fun onSuccess(success: Boolean?) {
//                                            LogManager.i(TAG, "success*****" + success!!)
//                                            manager.unSubscribe()
//                                        }
//
//                                        override fun onError(error: String) {
//                                            LogManager.i(TAG, "error*****$error")
//                                            manager.unSubscribe()
//                                        }
//                                    })
//                            val response: ProjectBean = GsonManager.getInstance().convert(jsonString, ProjectBean::class.java)

                            val response: ProjectBean =
                                GsonManager.getInstance().convert(success, ProjectBean::class.java)
                            if (response.data.datas != null && response.data.datas.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")

                                dataxRxFragmentSuccess.value = response.data.datas
                            } else {
                                dataxRxFragmentError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxRxFragmentError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxRxFragmentError.value = error
                    }
                })

//        compositeDisposable.add(disposable!!)
    }

    override fun getDataxRxFragmentSuccess(): SingleLiveData<List<DataX>> {
        return dataxRxFragmentSuccess
    }

    override fun getDataxRxFragmentError(): SingleLiveData<String> {
        return dataxRxFragmentError
    }

    override fun projectDataRxAppCompatActivity(
        rxAppCompatActivity: RxAppCompatActivity,
        currentPage: String
    ) {
        RetrofitManager.getInstance()
            .responseStringRxAppActivityBindUntilEvent(
                rxAppCompatActivity,
                model.projectData(currentPage),
                object : OnCommonSingleParamCallback<String> {
                    override fun onSuccess(success: String) {
                        LogManager.i(TAG, "success*****$success")
                        if (!TextUtils.isEmpty(success)) {
//                            val response2: ProjectBean = GsonManager.getInstance().convert(success, ProjectBean::class.java)
//                            response2.data.datas.get(0).author = null
//                            val jsonString: String = GsonManager.getInstance().toJson(response2)
//                            LogManager.i(TAG, "jsonString*****${jsonString}")
//                            val manager: ReadAndWriteManager = ReadAndWriteManager.getInstance()
//                            manager.writeExternal("mineLog.txt",
//                                    jsonString,
//                                    object : OnCommonSingleParamCallback<Boolean> {
//                                        override fun onSuccess(success: Boolean?) {
//                                            LogManager.i(TAG, "success*****" + success!!)
//                                            manager.unSubscribe()
//                                        }
//
//                                        override fun onError(error: String) {
//                                            LogManager.i(TAG, "error*****$error")
//                                            manager.unSubscribe()
//                                        }
//                                    })
//                            val response: ProjectBean = GsonManager.getInstance().convert(jsonString, ProjectBean::class.java)

                            val response: ProjectBean =
                                GsonManager.getInstance().convert(success, ProjectBean::class.java)
                            if (response.data.datas != null && response.data.datas.size > 0) {
//                                LogManager.i(TAG, "response*****${response.toString()}")

                                dataxRxAppCompatActivitySuccess.value = response.data.datas
                            } else {
                                dataxRxAppCompatActivityError.value =
                                    BaseApplication.getInstance().resources.getString(R.string.no_data_available)
                            }
                        } else {
                            dataxRxAppCompatActivityError.value =
                                BaseApplication.getInstance().resources.getString(R.string.loading_failed)
                        }
                    }

                    override fun onError(error: String) {
                        LogManager.i(TAG, "error*****$error")
                        dataxRxAppCompatActivityError.value = error
                    }
                })
    }

    override fun getDataxRxAppCompatActivitySuccess(): MutableLiveData<List<DataX>> {
        return dataxRxAppCompatActivitySuccess
    }

    override fun getDataxRxAppCompatActivityError(): MutableLiveData<String> {
        return dataxRxAppCompatActivityError
    }


}