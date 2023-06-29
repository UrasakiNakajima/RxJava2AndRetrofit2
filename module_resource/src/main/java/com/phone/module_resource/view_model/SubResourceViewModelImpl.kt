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
                                R.string.no_data_available
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