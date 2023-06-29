package com.phone.module_project.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.base.State
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
    val dataxRxFragment = SingleLiveData<State<MutableList<ArticleListBean>>>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivity = SingleLiveData<State<MutableList<ArticleListBean>>>()

    override fun subProjectData(
        pageNum: Int, tabId: Int
    ) {
        viewModelScope.launch { //开启viewModelScope.launch这种协程之后依然是在当前线程
            val apiResponse = executeRequest { model.subProjectData(pageNum, tabId) }

            //viewModelScope.launch开启协程之后，是在当前线程，然后上面那个IO线程执行完了，就会切换回当前线程
            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                apiResponse.data.let {
                    val list = ArticleListBean.trans(it?.datas ?: mutableListOf())
                    if (list.size > 0) {
                        dataxRxFragment.value = State.SuccessState(list)
                    } else {
                        dataxRxFragment.value = State.ErrorState(
                            BaseApplication.instance().resources.getString(
                                R.string.no_data_available
                            )
                        )
                    }
                }
            } else {
                dataxRxFragment.value =
                    State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun subProjectData2(
        pageNum: Int, tabId: Int
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
                                dataxRxActivity.value = State.SuccessState(list)
                            } else {
                                dataxRxActivity.value =
                                    State.ErrorState(
                                        BaseApplication.instance().resources.getString(
                                            R.string.no_data_available
                                        )
                                    )
                            }
                        }
                    } else {
                        dataxRxFragment.value = State.ErrorState(
                            BaseApplication.instance().resources.getString(
                                R.string.loading_failed
                            )
                        )
                    }
                }
            }
        }
    }

}


