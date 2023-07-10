package com.phone.module_project.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_base.BaseApplication
import com.phone.library_mvvm.BaseViewModel
import com.phone.library_network.bean.State
import com.phone.library_network.bean.ApiResponse
import com.phone.library_custom_view.bean.ArticleBean
import com.phone.library_custom_view.bean.ArticleListBean
import com.phone.library_network.manager.GsonManager
import com.phone.library_base.manager.LogManager
import com.phone.library_network.SingleLiveData
import com.phone.module_project.R
import com.phone.module_project.model.SubProjectModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SubProjectViewModelImpl : BaseViewModel(), ISubProjectViewModel {

    companion object {
        private val TAG: String = SubProjectViewModelImpl::class.java.simpleName
    }

    private val model = SubProjectModelImpl()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxFragment = SingleLiveData<State<MutableList<ArticleListBean>>>()

    //1.首先定义两个SingleLiveData的实例
    val dataxRxActivity = SingleLiveData<State<MutableList<ArticleListBean>>>()

    override fun subProjectData(
        pageNum: Int, tabId: Int
    ) {
        //在Android MVVM架构的ViewModel中启动一个新协程（如果你的项目架构是MVVM架构，则推荐在ViewModel中使用），
        //该协程默认运行在UI线程，协程和ViewModel的生命周期绑定，组件销毁时，协程一并销毁，从而实现安全可靠地协程调用。
        //调用viewModelScope.launch{} 或 viewModelScope.async{} 方法的时候可以指定运行线程（根据指定的线程来，不指定默认是UI线程）。
        viewModelScope.launch {
            val apiResponse = executeRequest { model.subProjectData(pageNum, tabId) }

            //viewModelScope.launch开启协程之后，是在当前线程，然后上面那个IO线程执行完了，就会切换回当前线程
            if (apiResponse.data != null && apiResponse.errorCode == 0) {
                apiResponse.data.let {
                    val list = ArticleListBean.trans(it?.datas ?: mutableListOf())
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
                dataxRxFragment.value =
                    State.ErrorState(apiResponse.errorMsg)
            }
        }
    }

    override fun subProjectData2(
        pageNum: Int, tabId: Int
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.subProjectData2(pageNum, tabId).execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "subProjectData response pageNum$pageNum*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<ArticleBean>>() {}.type
                        val response: ApiResponse<ArticleBean> =
                            GsonManager().fromJson(success ?: "", type2)
                        response.data().let {
                            val list = ArticleListBean.trans(it.datas ?: mutableListOf())
                            if (list.size > 0) {
                                dataxRxActivity.value = State.SuccessState(list)
                            } else {
                                dataxRxActivity.value =
                                    State.ErrorState(
                                        BaseApplication.instance().resources.getString(
                                            R.string.library_no_data_available
                                        )
                                    )
                            }
                        }
                    } else {
                        dataxRxFragment.value = State.ErrorState(
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


