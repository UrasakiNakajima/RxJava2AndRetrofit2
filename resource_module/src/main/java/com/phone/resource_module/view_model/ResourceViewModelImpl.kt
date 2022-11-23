package com.phone.resource_module.view_model

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
import com.phone.resource_module.R
import com.phone.resource_module.model.ResourceModelImpl
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
                    } else {
                        tabRxFragmentError.value =
                            BaseApplication.getInstance().resources.getString(
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