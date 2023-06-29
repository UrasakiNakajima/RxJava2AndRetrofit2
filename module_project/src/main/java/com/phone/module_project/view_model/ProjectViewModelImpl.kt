package com.phone.module_project.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.base.State
import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.ArticleListBean
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.ResourcesManager
import com.phone.library_common.manager.SingleLiveData
import com.phone.module_project.R
import com.phone.module_project.model.ProjectModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModelImpl : BaseViewModel(), IProjectViewModel {

    private val TAG = ProjectViewModelImpl::class.java.simpleName

    private val model = ProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragment = SingleLiveData<State<MutableList<TabBean>>>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivity = SingleLiveData<State<MutableList<TabBean>>>()

    override fun projectTabData() {
        viewModelScope.launch {
            val apiResponse = executeRequest { model.projectTabData() }
            if (apiResponse.errorCode == 0) {
                val responseData = apiResponse.data ?: mutableListOf()
                if (responseData.size > 0) {
                    dataxRxFragment.value = State.SuccessState(responseData)
                } else {
                    dataxRxFragment.value =
                        State.ErrorState(ResourcesManager.getString(R.string.no_data_available))
                }
            } else {
                dataxRxFragment.value =
                    State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun projectTabData2() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.projectTabData2().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "projectTabData2 response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success ?: "", type2)
                        response.let {
                            if (response.data().size > 0) {
                                dataxRxActivity.value = State.SuccessState(response.data())
                            } else {
                                dataxRxActivity.value = State.ErrorState(
                                    BaseApplication.instance().resources.getString(
                                        R.string.no_data_available
                                    )
                                )

                            }
                        }
                    } else {
                        dataxRxActivity.value = State.ErrorState(
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
