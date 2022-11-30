package com.phone.module_resource.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
import com.phone.library_common.manager.SingleLiveData
import com.phone.module_resource.R
import com.phone.module_resource.model.ResourceModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResourceViewModelImpl() : BaseViewModel(), IResourceViewModel {

    companion object {
        private val TAG: String = ResourceViewModelImpl::class.java.simpleName
    }

    private val model = ResourceModelImpl()

    val tabRxFragmentSuccess = SingleLiveData<MutableList<TabBean>>()
    val tabRxFragmentError = SingleLiveData<String>()

    val tabRxActivitySuccess = SingleLiveData<MutableList<TabBean>>()
    val tabRxActivityError = SingleLiveData<String>()

    override fun resourceTabData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success =
                    model.resourceTabData().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "resourceTabData response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success, type2)
                        if (response.data().size > 0) {
                            tabRxFragmentSuccess.value = response.data()
                        } else {
                            tabRxFragmentError.value =
                                BaseApplication.get().resources.getString(
                                    R.string.no_data_available
                                )
                        }
                    } else {
                        tabRxFragmentError.value =
                            BaseApplication.get().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }
    }

    override fun resourceTabData2() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success =
                    model.resourceTabData().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "resourceTabData response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success, type2)
                        if (response.data().size > 0) {
                            tabRxActivitySuccess.value = response.data()
                        } else {
                            tabRxActivityError.value =
                                BaseApplication.get().resources.getString(
                                    R.string.no_data_available
                                )
                        }
                    } else {
                        tabRxActivityError.value =
                            BaseApplication.get().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }
    }

}