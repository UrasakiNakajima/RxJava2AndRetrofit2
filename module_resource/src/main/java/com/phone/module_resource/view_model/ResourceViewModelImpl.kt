package com.phone.module_resource.view_model

import androidx.lifecycle.viewModelScope
import com.phone.library_base.BaseApplication
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.bean.State
import com.phone.library_common.bean.TabBean
import com.phone.library_network.SingleLiveData
import com.phone.module_resource.R
import com.phone.module_resource.model.ResourceModelImpl
import kotlinx.coroutines.launch

class ResourceViewModelImpl() : BaseViewModel(), IResourceViewModel {

    companion object {
        private val TAG: String = ResourceViewModelImpl::class.java.simpleName
    }

    private val model = ResourceModelImpl()

    val tabRxFragment = SingleLiveData<State<MutableList<TabBean>>>()
    val tabRxActivity = SingleLiveData<State<MutableList<TabBean>>>()

    override fun resourceTabData() {
        //在Android MVVM架构的ViewModel中启动一个新协程（如果你的项目架构是MVVM架构，则推荐在ViewModel中使用），
        //该协程默认运行在UI线程，协程和ViewModel的生命周期绑定，组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch{} 或 viewModelScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        viewModelScope.launch {
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

}