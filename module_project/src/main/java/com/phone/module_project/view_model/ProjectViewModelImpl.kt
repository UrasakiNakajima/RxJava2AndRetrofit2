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
        //在Android MVVM架构的ViewModel中启动一个新协程（如果你的项目架构是MVVM架构，则推荐在ViewModel中使用），
        //该协程默认运行在UI线程，协程和ViewModel的生命周期绑定，组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch{} 或 viewModelScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        viewModelScope.launch {
            val apiResponse = executeRequest { model.projectTabData() }
            if (apiResponse.errorCode == 0) {
                val responseData = apiResponse.data ?: mutableListOf()
                if (responseData.size > 0) {
                    dataxRxFragment.value = State.SuccessState(responseData)
                } else {
                    dataxRxFragment.value =
                        State.ErrorState(ResourcesManager.getString(R.string.library_no_data_available))
                }
            } else {
                dataxRxFragment.value =
                    State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun projectTabData2() {
        //在Android MVVM架构的viewModel中启动一个新协程（推荐使用），该协程默认运行在UI线程，协程和该组件生命周期绑定，
        //组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch {} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
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
                                        R.string.library_no_data_available
                                    )
                                )

                            }
                        }
                    } else {
                        dataxRxActivity.value = State.ErrorState(
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
