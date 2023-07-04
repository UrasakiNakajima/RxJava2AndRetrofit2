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
        //在Android MVVM架构的viewModel中启动一个新协程（推荐使用），该协程默认运行在UI线程，协程和该组件生命周期绑定，
        //组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch {} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
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
                                R.string.library_no_data_available
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
                                        R.string.library_no_data_available
                                    )
                                )
                        }
                    } else {
                        tabRxActivity.value =
                            State.ErrorState(
                                BaseApplication.instance().resources.getString(
                                    R.string.library_loading_failed
                                )
                            )
                    }
                }
            }
        }
    }

}