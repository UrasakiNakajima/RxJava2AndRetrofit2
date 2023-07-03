package com.phone.module_resource.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.base.State
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

    val tabRxFragment = SingleLiveData<State<MutableList<TabBean>>>()
    val tabRxActivity = SingleLiveData<State<MutableList<TabBean>>>()

    override fun resourceTabData() {
        viewModelScope.launch { //开启viewModelScope.launch这种协程之后依然是在当前线程
            val apiResponse = executeRequest { model.resourceTabData() }
            //viewModelScope.launch开启协程之后，是在当前线程，然后上面那个IO线程执行完了，就会切换回当前线程
            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                apiResponse.data.also {
                    val list = it ?: mutableListOf()
                    if (list.size > 0) {
                        tabRxFragment.value = State.SuccessState(list)
                    } else {
                        tabRxFragment.value = State.ErrorState(
                            BaseApplication.instance().resources.getString(
                                R.string.no_data_available
                            )
                        )
                    }
                }
            } else {
                tabRxFragment.value = State.ErrorState(
                    apiResponse.errorMsg
                )
            }
        }
    }

    override fun resourceTabData2() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success =
                    model.resourceTabData2().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "resourceTabData response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success ?: "", type2)
                        if (response.data().size > 0) {
                            tabRxActivity.value =
                                State.SuccessState(response.data ?: mutableListOf())
                        } else {
                            tabRxActivity.value =
                                State.ErrorState(
                                    BaseApplication.instance().resources.getString(
                                        R.string.no_data_available
                                    )
                                )
                        }
                    } else {
                        tabRxActivity.value =
                            State.ErrorState(
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