package com.phone.module_project.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.ArticleBean
import com.phone.library_common.bean.ArticleListBean
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SingleLiveData
import com.phone.module_project.R
import com.phone.module_project.model.SubProjectModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SubProjectViewModelImpl : BaseViewModel(), ISubProjectViewModel {

    companion object {
        private val TAG: String = SubProjectViewModelImpl::class.java.simpleName
    }

    private val model = SubProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess = SingleLiveData<MutableList<ArticleListBean>>()
    val dataxRxFragmentError = SingleLiveData<String>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivitySuccess = SingleLiveData<MutableList<ArticleListBean>>()
    val dataxRxActivityError = SingleLiveData<String>()

    override fun subProjectData(
        pageNum: Int,
        tabId: Int
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.subProjectData2(pageNum, tabId).execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "subProjectData response pageNum$pageNum*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<ArticleBean>>() {}.type
                        val response: ApiResponse<ArticleBean> =
                            GsonManager().fromJson(success ?: "", type2)
                        response.data().let {
                            val list = ArticleListBean.trans(it.datas ?: mutableListOf())
                            if (list.size > 0) {
                                dataxRxFragmentSuccess.value = list
                            } else {
                                dataxRxFragmentError.value =
                                    BaseApplication.get().resources.getString(
                                        R.string.no_data_available
                                    )
                            }
                        }
                    } else {
                        dataxRxFragmentError.value =
                            BaseApplication.get().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }

//        RetrofitManager.getInstance()
//            .responseStringAutoDispose(
//                rxFragment,
//                model.subProjectData(currentPage),
//                object : OnCommonSingleParamCallback<String> {
//                    override fun onSuccess(success: String) {
//                        LogManager.i(TAG, "success*****$success")
//                        if (!TextUtils.isEmpty(success)) {
////                            val response2: ProjectBean = new GsonManager().convert(success, ProjectBean::class.java)
////                            response2.data.datas.get(0).author = null
////                            val jsonString: String = new GsonManager().toJson(response2)
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
////                            val response: ProjectBean = new GsonManager().convert(jsonString, ProjectBean::class.java)
//
//                            val response: ProjectBean =
//                                new GsonManager().convert(success, ProjectBean::class.java)
//                            if (response.data.datas != null && response.data.datas.size > 0) {
////                                LogManager.i(TAG, "response*****${response.toString()}")
//
//                                dataxRxFragmentSuccess.value = response.data.datas
//                            } else {
//                                dataxRxFragmentError.value =
//                                    BaseApplication.get().resources.getString(R.string.no_data_available)
//                            }
//                        } else {
//                            dataxRxFragmentError.value =
//                                BaseApplication.get().resources.getString(R.string.loading_failed)
//                        }
//                    }
//
//                    override fun onError(error: String) {
//                        LogManager.i(TAG, "error*****$error")
//                        dataxRxFragmentError.value = error
//                    }
//                })
    }

    override fun subProjectData2(
        pageNum: Int,
        tabId: Int
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.subProjectData2(pageNum, tabId).execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "subProjectData response pageNum$pageNum*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<ArticleBean>>() {}.type
                        val response: ApiResponse<ArticleBean> =
                            GsonManager().fromJson(success ?: "", type2)
                        response.data().let {
                            val list = ArticleListBean.trans(it.datas ?: mutableListOf())
                            if (list.size > 0) {
                                dataxRxActivitySuccess.value = list
                            } else {
                                dataxRxActivityError.value =
                                    BaseApplication.get().resources.getString(
                                        R.string.no_data_available
                                    )
                            }
                        }
                    } else {
                        dataxRxFragmentError.value =
                            BaseApplication.get().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }
    }


}