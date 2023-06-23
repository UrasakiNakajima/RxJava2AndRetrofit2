package com.phone.module_project.view_model

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.phone.library_common.BaseApplication
import com.phone.library_common.base.BaseViewModel
import com.phone.library_common.bean.ApiResponse
import com.phone.library_common.bean.TabBean
import com.phone.library_common.manager.GsonManager
import com.phone.library_common.manager.LogManager
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
    val tabRxFragmentSuccess = SingleLiveData<MutableList<TabBean>>()
    val tabRxFragmentError = SingleLiveData<String>()

    //1.首先定义两个SingleLiveData的实例
    val tabRxActivitySuccess = SingleLiveData<MutableList<TabBean>>()
    val tabRxActivityError = SingleLiveData<String>()

    override fun projectTabData() {
        viewModelScope.launch {
            //开启viewModelScope.launch这种协程之后依然是在当前线程
            LogManager.i(TAG, "projectTabData name*****${Thread.currentThread().name}")
            var response: ApiResponse<MutableList<TabBean>>
            withContext(Dispatchers.IO) {
                //切换到IO线程
                val success = model.projectTabData().execute().body()?.string()
                val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                response = GsonManager().fromJson(success ?: "", type2)
                LogManager.i(TAG, "withContext name*****${Thread.currentThread().name}")
            }

            //viewModelScope.launch开启协程之后，是在当前线程，然后上面那个IO线程执行完了，就会切换回当前线程
            LogManager.i(TAG, "projectTabData2 name*****${Thread.currentThread().name}")
            response.data().let {
                LogManager.i(TAG, "projectTabData2 it*****${it.toString()}")
                if (it.size > 0) {
                    tabRxFragmentSuccess.value = it
                } else {
                    tabRxFragmentError.value = BaseApplication.instance().resources.getString(
                        R.string.no_data_available
                    )
                }
            }
        }

//        viewModelScope.launch {
//            val tabBeanList = projectTabDataSuspend()
//            LogManager.i(TAG, "tabBeanList thread name*****${Thread.currentThread().name}")
//            tabRxFragmentSuccess.value = tabBeanList
//        }
    }

    /**
     * 在协程或者挂起函数里调用，挂起函数里必须要切换到线程（这里切换到IO线程）
     */
    suspend fun projectTabDataSuspend(): MutableList<TabBean> {
        val tabBeanList = mutableListOf<TabBean>()
        LogManager.i(TAG, "projectTabDataNew thread name*****${Thread.currentThread().name}")
        withContext(Dispatchers.IO) {
            val success = model.projectTabData().execute().body()?.string()
            val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
            val response: ApiResponse<MutableList<TabBean>> =
                GsonManager().fromJson(success ?: "", type2)
            LogManager.i(TAG, "withContext thread name*****${Thread.currentThread().name}")

            response.data().let {
                if (it.size > 0) {
                    tabBeanList.clear()
                    tabBeanList.addAll(it)
                }
            }
        }

        return tabBeanList
    }


    override fun projectTabData2() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val success = model.projectTabData().execute().body()?.string()
                launch(Dispatchers.Main) {
                    LogManager.i(TAG, "resourceTabData response*****$success")
                    if (!TextUtils.isEmpty(success)) {
                        val type2 = object : TypeToken<ApiResponse<MutableList<TabBean>>>() {}.type
                        val response: ApiResponse<MutableList<TabBean>> =
                            GsonManager().fromJson(success ?: "", type2)
                        response.let {
                            if (response.data().size > 0) {
                                tabRxActivitySuccess.value = response.data()
                            } else {
                                tabRxActivityError.value =
                                    BaseApplication.instance().resources.getString(
                                        R.string.no_data_available
                                    )
                            }
                        }
                    } else {
                        tabRxActivityError.value = BaseApplication.instance().resources.getString(
                            R.string.loading_failed
                        )
                    }
                }
            }
        }
    }

}