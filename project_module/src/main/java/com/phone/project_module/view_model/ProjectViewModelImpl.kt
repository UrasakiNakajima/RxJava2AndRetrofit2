package com.phone.project_module.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseViewModel
import com.phone.common_library.bean.ApiResponse
import com.phone.common_library.bean.TabBean
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.SingleLiveData
import com.phone.project_module.R
import com.phone.project_module.model.ProjectModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectViewModelImpl : BaseViewModel(), IProjectViewModel {

    private val TAG = ProjectViewModelImpl::class.java.simpleName

    private val model = ProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val tabRxFragmentSuccess: SingleLiveData<MutableList<TabBean>> = SingleLiveData()
    val tabRxFragmentError: SingleLiveData<String> = SingleLiveData()

    //1.首先定义两个SingleLiveData的实例
    val tabRxActivitySuccess: SingleLiveData<MutableList<TabBean>> = SingleLiveData()
    val tabRxActivityError: SingleLiveData<String> = SingleLiveData()

    override fun projectTabData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.projectTabData().execute().body()?.string()
                val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                val response: ApiResponse<MutableList<TabBean>> =
                    GsonManager().fromJson(success, type2)
                LogManager.i(TAG, "thread name*****${Thread.currentThread().name}")

                launch(Dispatchers.Main) {
                    response.data().let {
                        if (it.size > 0) {
                            tabRxFragmentSuccess.value = it
                        } else {
                            tabRxFragmentError.value =
                                BaseApplication.getInstance().resources.getString(
                                    R.string.no_data_available
                                )
                        }
                    }
                }
            }
        }
    }


    override fun projectTabData2() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success =
                    model.projectTabData().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "resourceTabData response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success, type2)
                        response.let {
                            if (response.data().size > 0) {
                                tabRxActivitySuccess.value = response.data()
                            } else {
                                tabRxActivityError.value =
                                    BaseApplication.getInstance().resources.getString(
                                        R.string.no_data_available
                                    )
                            }
                        }
                    } else {
                        tabRxActivityError.value =
                            BaseApplication.getInstance().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }
    }

}