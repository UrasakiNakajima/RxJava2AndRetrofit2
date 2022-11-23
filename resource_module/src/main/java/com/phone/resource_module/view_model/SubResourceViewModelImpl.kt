package com.phone.square_module.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.common_library.BaseApplication
import com.phone.common_library.base.BaseViewModel
import com.phone.common_library.bean.ApiResponse
import com.phone.common_library.bean.ArticleBean
import com.phone.common_library.bean.ArticleListBean
import com.phone.common_library.manager.GsonManager
import com.phone.common_library.manager.LogManager
import com.phone.common_library.manager.SingleLiveData
import com.phone.resource_module.R
import com.phone.resource_module.model.SubResourceModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SubResourceViewModelImpl() : BaseViewModel(), ISubResourceViewModel {

    companion object {
        private val TAG: String = SubResourceViewModelImpl::class.java.simpleName
    }

    private var model = SubResourceModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragmentSuccess = SingleLiveData<MutableList<ArticleListBean>>()
    val dataxRxFragmentError = SingleLiveData<String>()

    override fun subResourceData(
        tabId: Int,
        pageNum: Int
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success =
                    model.subResourceData2(tabId, pageNum).execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "subResourceData response pageNum$pageNum*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<ArticleBean>>() {}.type
                        val response: ApiResponse<ArticleBean> =
                            GsonManager().fromJson(success, type2)
                        response.data().let {
                            val list = ArticleListBean.trans(it.datas ?: mutableListOf())
                            if (list.size > 0) {
                                dataxRxFragmentSuccess.value = list
                            } else {
                                dataxRxFragmentError.value =
                                    BaseApplication.getInstance().resources.getString(
                                        R.string.no_data_available
                                    )
                            }
                        }
                    } else {
                        dataxRxFragmentError.value =
                            BaseApplication.getInstance().resources.getString(
                                R.string.loading_failed
                            )
                    }
                }
            }
        }
    }

}