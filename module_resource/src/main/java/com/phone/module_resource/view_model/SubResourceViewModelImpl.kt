package com.phone.module_square.view_model

import androidx.lifecycle.viewModelScope
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.base.State
import com.phone.library_common.bean.ArticleListBean
import com.phone.library_common.manager.SingleLiveData
import com.phone.module_resource.R
import com.phone.module_resource.model.SubResourceModelImpl
import kotlinx.coroutines.launch


class SubResourceViewModelImpl() : BaseViewModel(), ISubResourceViewModel {

    companion object {
        private val TAG: String = SubResourceViewModelImpl::class.java.simpleName
    }

    private var model = SubResourceModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragment = SingleLiveData<State<MutableList<ArticleListBean>>>()

    override fun subResourceData(
        tabId: Int,
        pageNum: Int
    ) {
        //在Android MVVM架构的viewModel中启动一个新协程（推荐使用），该协程默认运行在UI线程，协程和该组件生命周期绑定，
        //组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch {} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        viewModelScope.launch {
            val apiResponse = executeRequest { model.subResourceData(tabId, pageNum) }
            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                apiResponse.also {
                    val list = ArticleListBean.trans(it.data?.datas ?: mutableListOf())
                    if (list.size > 0) {
                        dataxRxFragment.value = State.SuccessState(list)
                    } else {
                        dataxRxFragment.value = State.ErrorState(
                            BaseApplication.instance().resources.getString(
                                R.string.library_no_data_available
                            )
                        )
                    }
                }
            } else {
                dataxRxFragment.value = State.ErrorState(
                    apiResponse.errorMsg
                )
            }
        }
    }

}