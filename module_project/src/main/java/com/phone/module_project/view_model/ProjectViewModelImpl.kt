package com.phone.module_project.view_model

import androidx.lifecycle.viewModelScope
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.bean.State
import com.phone.call_third_party_so.bean.TabBean
import com.phone.library_base.manager.ResourcesManager
import com.phone.library_network.SingleLiveData
import com.phone.module_project.R
import com.phone.module_project.model.ProjectModelImpl
import kotlinx.coroutines.launch

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

}
